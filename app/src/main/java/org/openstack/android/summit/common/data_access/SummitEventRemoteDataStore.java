package org.openstack.android.summit.common.data_access;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.api.ISummitEventsApi;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializer;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import java.util.List;

import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import retrofit2.Retrofit;

/**
 * Created by Claudio Redi on 2/19/2016.
 */
public class SummitEventRemoteDataStore extends BaseRemoteDataStore implements ISummitEventRemoteDataStore {

    private IDeserializer    deserializer;
    private Retrofit         restClient;
    private ISummitEventsApi summitEventsApi;
    private ISummitSelector  summitSelector;

    public SummitEventRemoteDataStore(IDeserializer deserializer,
                                      @Named("ServiceProfileRXJava2") Retrofit restClient,
                                      ISummitSelector summitSelector) {
        this.deserializer    = deserializer;
        this.restClient      = restClient;
        this.summitEventsApi = this.restClient.create(ISummitEventsApi.class);
        this.summitSelector  = summitSelector;
    }

    @Override
    public Observable<List<Feedback>> getFeedback(int eventId, int page, int objectsPerPage) {

       return summitEventsApi
               .getEventFeedback(summitSelector.getCurrentSummitId(), eventId, "owner", page, objectsPerPage)
               .subscribeOn(Schedulers.io())
               .map( response -> {
                    if(!response.isSuccessful()){
                         throw new Exception
                                (
                                        String.format
                                                (
                                                        "getFeedback: http error %d",
                                                        response.code()
                                                )
                                );
                    }
                    return RealmFactory.transaction(session -> deserializer.deserializePage(response.body().string(), Feedback.class));
                }).doOnTerminate( () ->
                   RealmFactory.closeSession()
               );
    }

    @Override
    public Observable<Double> getAverageFeedback(int eventId) {

        return summitEventsApi.
                getPublishedEvent(summitSelector.getCurrentSummitId(), eventId, "id,avg_feedback_rate", "none")
                .subscribeOn(Schedulers.io())
                .map( response -> {
                    if(!response.isSuccessful()){
                        throw new Exception
                                (
                                        String.format
                                                (
                                                        "getAverageFeedback: http error %d",
                                                        response.code()
                                                )
                                );
                    }

                    final JSONObject json = new JSONObject(response.body().string());

                    SummitEvent summitEvent = RealmFactory.transaction(session -> {
                        SummitEvent summitEvent1 = session.where(SummitEvent.class).equalTo("id", json.getInt("id")).findFirst();
                        Double avgRateFromServer = json.optDouble("avg_feedback_rate");
                        updateAverageRateIfNecessary(summitEvent1, avgRateFromServer);
                        return summitEvent1;
                    });

                    return summitEvent.getAverageRate();
                }).doOnTerminate( () ->
                        RealmFactory.closeSession()
                );
    }

    @Override
    public Observable<SummitEvent> getSummitEventById(int eventId) {
        return summitEventsApi
                .getPublishedEvent(summitSelector.getCurrentSummitId(), eventId, "", "")
                .subscribeOn(Schedulers.io())
                .map( response -> {

                    SummitEvent summitEvent = null;
                    int code      = response.code();
                    try {
                        if(!response.isSuccessful()){
                            throw new Exception(String.format("SummitEventRemoteDataStore.getSummitEventById http code %d", code));
                        }

                        final String data = response.body().string();


                        summitEvent = RealmFactory.transaction(session ->
                                session.copyToRealmOrUpdate(deserializer.deserialize(data, SummitEvent.class))
                        );

                    } catch (Exception ex) {
                        Crashlytics.logException(ex);
                        Crashlytics.log(String.format("SummitEventRemoteDataStore.getSummitEventById http code %d", code));
                        Log.e(Constants.LOG_TAG, ex.getMessage(), ex);
                        throw Exceptions.propagate(ex);
                    }
                    return summitEvent;
                }).doOnTerminate( () ->
                        RealmFactory.closeSession()
                );
    }

    private void updateAverageRateIfNecessary(final SummitEvent summitEvent, final Double averateRateFromServer) throws DataAccessException {
        if (summitEvent != null && !averateRateFromServer.isNaN() && summitEvent.getAverageRate() != averateRateFromServer) {

            RealmFactory.transaction(session -> {
                summitEvent.setAverageRate(averateRateFromServer);
                return Void.getInstance();
            });
        }
    }
}
