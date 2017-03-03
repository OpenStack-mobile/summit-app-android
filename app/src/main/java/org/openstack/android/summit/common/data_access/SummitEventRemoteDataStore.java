package org.openstack.android.summit.common.data_access;

import org.json.JSONObject;
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
import io.reactivex.schedulers.Schedulers;
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
                });
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
                });
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
