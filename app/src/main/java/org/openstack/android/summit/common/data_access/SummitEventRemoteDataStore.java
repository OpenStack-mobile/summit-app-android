package org.openstack.android.summit.common.data_access;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.api_endpoints.ApiEndpointBuilder;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializer;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.network.HttpTask;
import org.openstack.android.summit.common.network.HttpTaskListener;
import org.openstack.android.summit.common.network.IHttpTaskFactory;
import org.openstack.android.summit.common.security.AccountType;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;

/**
 * Created by Claudio Redi on 2/19/2016.
 */
public class SummitEventRemoteDataStore extends BaseRemoteDataStore implements ISummitEventRemoteDataStore {
    private IDeserializer deserializer;
    private IHttpTaskFactory httpTaskFactory;

    public SummitEventRemoteDataStore(IHttpTaskFactory httpTaskFactory, IDeserializer deserializer) {
        this.httpTaskFactory = httpTaskFactory;
        this.deserializer = deserializer;
    }

    @Override
    public void getFeedback(int eventId, int page, int objectsPerPage, final IDataStoreOperationListener<Feedback> dataStoreOperationListener) {
        try {
            HttpTaskListener httpTaskListener = new HttpTaskListener() {
                @Override
                public void onSucceed(String data) {
                    try {
                        List<Feedback> feedbackList = deserializer.deserializePage(data, Feedback.class);
                        dataStoreOperationListener.onSucceedWithDataCollection(feedbackList);
                    } catch (JSONException e) {
                        Crashlytics.logException(e);
                        Log.e(Constants.LOG_TAG, "Error deserializing feedback", e);
                        String friendlyError = Constants.GENERIC_ERROR_MSG;
                        dataStoreOperationListener.onError(friendlyError);
                    }
                }

                @Override
                public void onError(Throwable error) {
                    String friendlyError = Constants.GENERIC_ERROR_MSG;
                    dataStoreOperationListener.onError(friendlyError);
                }
            };

            Map<String, Object> params = new HashMap<>();
            params.put(ApiEndpointBuilder.EventIdParam, eventId);
            params.put(ApiEndpointBuilder.ExpandParam, "owner");
            params.put(ApiEndpointBuilder.PageParam, page);
            params.put(ApiEndpointBuilder.PerPageParam, objectsPerPage);

            HttpTask httpTask = httpTaskFactory.create
                    (
                            AccountType.ServiceAccount,
                            ApiEndpointBuilder.getInstance().buildEndpoint(getBaseResourceServerUrl(), "current", ApiEndpointBuilder.EndpointType.GetFeedback, params).toString(),
                            HttpRequest.METHOD_GET,
                            null,
                            null,
                            httpTaskListener
                    );
            httpTask.execute();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            String friendlyError = Constants.GENERIC_ERROR_MSG;
            dataStoreOperationListener.onError(friendlyError);
        }
    }

    @Override
    public void getAverageFeedback(int eventId, final IDataStoreOperationListener<SummitEvent> dataStoreOperationListener) {
        try {
            HttpTaskListener httpTaskListener = new HttpTaskListener() {
                @Override
                public void onSucceed(String data) {
                    try {
                        final JSONObject json = new JSONObject(data);

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
                    } catch (Exception e) {
                        Crashlytics.logException(e);
                        Log.e(Constants.LOG_TAG, e.getMessage(), e);
                        String friendlyError = Constants.GENERIC_ERROR_MSG;
                        dataStoreOperationListener.onError(friendlyError);
                    }
                }

                @Override
                public void onError(Throwable error) {
                    String friendlyError = Constants.GENERIC_ERROR_MSG;
                    dataStoreOperationListener.onError(friendlyError);
                }
            };

            Map<String, Object> params = new HashMap<>();
            params.put(ApiEndpointBuilder.EventIdParam, eventId);
            params.put(ApiEndpointBuilder.FieldsParam, "id,avg_feedback_rate");
            params.put(ApiEndpointBuilder.RelationsParam, "none");

            HttpTask httpTask = httpTaskFactory.create
                    (
                            AccountType.ServiceAccount,
                            ApiEndpointBuilder.getInstance().buildEndpoint(getBaseResourceServerUrl(), "current", ApiEndpointBuilder.EndpointType.GetPublishedEvent, params).toString(),
                            HttpRequest.METHOD_GET,
                            null,
                            null,
                            httpTaskListener
                    );
            httpTask.execute();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            String friendlyError = Constants.GENERIC_ERROR_MSG;
            dataStoreOperationListener.onError(friendlyError);
        }
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
