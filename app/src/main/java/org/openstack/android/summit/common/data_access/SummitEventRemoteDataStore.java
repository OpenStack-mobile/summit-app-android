package org.openstack.android.summit.common.data_access;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.api.ISummitEventsApi;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.api.SummitSelector;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializer;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import java.util.List;

import javax.inject.Named;

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Claudio Redi on 2/19/2016.
 */
public class SummitEventRemoteDataStore extends BaseRemoteDataStore implements ISummitEventRemoteDataStore {

    private IDeserializer    deserializer;
    private Retrofit         restClient;
    private ISummitEventsApi summitEventsApi;
    private ISummitSelector summitSelector;

    public SummitEventRemoteDataStore(IDeserializer deserializer,
                                      @Named("ServiceProfile") Retrofit restClient,
                                      ISummitSelector summitSelector) {
        this.deserializer    = deserializer;
        this.restClient      = restClient;
        this.summitEventsApi = this.restClient.create(ISummitEventsApi.class);
        this.summitSelector  = summitSelector;
    }

    @Override
    public void getFeedback(int eventId, int page, int objectsPerPage, final IDataStoreOperationListener<Feedback> dataStoreOperationListener) {

        Call<ResponseBody> call = this.summitEventsApi.getEventFeedback(summitSelector.getCurrentSummitId(), eventId, "owner", page, objectsPerPage);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                try{
                    List<Feedback> feedbackList = RealmFactory.transaction(new RealmFactory.IRealmCallback<List<Feedback> >() {
                        @Override
                        public List<Feedback> callback(Realm session) throws Exception {
                            return deserializer.deserializePage(response.body().string(), Feedback.class);
                        }
                    });

                    dataStoreOperationListener.onSucceedWithDataCollection(feedbackList);

                }
                catch (Exception ex){
                    Crashlytics.logException(ex);
                    Log.e(Constants.LOG_TAG, ex.getMessage(), ex);
                    String friendlyError = Constants.GENERIC_ERROR_MSG;
                    dataStoreOperationListener.onError(friendlyError);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Crashlytics.logException(t);
                Log.e(Constants.LOG_TAG, t.getMessage(), t);
                String friendlyError = Constants.GENERIC_ERROR_MSG;
                dataStoreOperationListener.onError(friendlyError);
            }
        });
    }

    @Override
    public void getAverageFeedback(int eventId, final IDataStoreOperationListener<SummitEvent> dataStoreOperationListener) {

        Call<ResponseBody> call = this.summitEventsApi.getPublishedEvent(summitSelector.getCurrentSummitId(), eventId, "id,avg_feedback_rate", "none");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               try{
                   final JSONObject json = new JSONObject(response.body().string());

                   SummitEvent summitEvent = RealmFactory.transaction(new RealmFactory.IRealmCallback<SummitEvent>() {
                       @Override
                       public SummitEvent callback(Realm session) throws Exception {
                           SummitEvent summitEvent = session.where(SummitEvent.class).equalTo("id", json.getInt("id")).findFirst();
                           Double avgRateFromServer = json.optDouble("avg_feedback_rate");
                           updateAverageRateIfNecessary(summitEvent, avgRateFromServer);
                           return summitEvent;
                       }
                   });
                   dataStoreOperationListener.onSucceedWithSingleData(summitEvent);
               }
               catch (Exception ex)
               {
                   Crashlytics.logException(ex);
                   Log.e(Constants.LOG_TAG, ex.getMessage(), ex);
                   String friendlyError = Constants.GENERIC_ERROR_MSG;
                   dataStoreOperationListener.onError(friendlyError);
               }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Crashlytics.logException(t);
                Log.e(Constants.LOG_TAG, t.getMessage(), t);
                String friendlyError = Constants.GENERIC_ERROR_MSG;
                dataStoreOperationListener.onError(friendlyError);
            }
        });
    }

    private void updateAverageRateIfNecessary(final SummitEvent summitEvent, final Double averateRateFromServer) throws DataAccessException {
        if (summitEvent != null && !averateRateFromServer.isNaN() && summitEvent.getAverageRate() != averateRateFromServer) {

            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception {
                    summitEvent.setAverageRate(averateRateFromServer);
                    return Void.getInstance();
                }
            });
        }
    }
}
