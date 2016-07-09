package org.openstack.android.summit.common.data_access;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializer;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.network.HttpTask;
import org.openstack.android.summit.common.network.HttpTaskListener;
import org.openstack.android.summit.common.network.IHttpTaskFactory;
import org.openstack.android.summit.common.security.AccountType;

import java.util.List;

import io.realm.Realm;

/**
 * Created by Claudio Redi on 2/19/2016.
 */
public class SummitEventRemoteDataStore extends BaseRemoteDataStore implements ISummitEventRemoteDataStore {
    private IDeserializer deserializer;
    private IHttpTaskFactory httpTaskFactory;
    public Realm realm = Realm.getDefaultInstance();

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
            String url = getResourceServerUrl() +
                    String.format("/api/v1/summits/current/events/%d/feedback?expand=owner&page=%d&per_page=%d", eventId, page, objectsPerPage);
            HttpTask httpTask = httpTaskFactory.create(AccountType.ServiceAccount, url, HttpRequest.METHOD_GET, null, null, httpTaskListener);
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
                        JSONObject json = new JSONObject(data);
                        SummitEvent summitEvent = realm.where(SummitEvent.class).equalTo("id", json.getInt("id")).findFirst();
                        Double averateRateFromServer = json.optDouble("avg_feedback_rate");
                        updateAverageRateIfNecessary(summitEvent, averateRateFromServer);
                        dataStoreOperationListener.onSuceedWithSingleData(summitEvent);
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
            String url = getResourceServerUrl() +
                    String.format("/api/v1/summits/current/events/%d/published?fields=id,avg_feedback_rate&relations=none", eventId);
            HttpTask httpTask = httpTaskFactory.create(AccountType.ServiceAccount, url, HttpRequest.METHOD_GET, null, null, httpTaskListener);
            httpTask.execute();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            String friendlyError = Constants.GENERIC_ERROR_MSG;
            dataStoreOperationListener.onError(friendlyError);
        }
    }

    private void updateAverageRateIfNecessary(SummitEvent summitEvent, Double averateRateFromServer) {
        try{
            if (summitEvent != null && !averateRateFromServer.isNaN() && summitEvent.getAverageRate() != averateRateFromServer) {
                realm.beginTransaction();
                summitEvent.setAverageRate(averateRateFromServer);
                realm.commitTransaction();
            }
        }
        catch (Exception e) {
            realm.cancelTransaction();
            throw e;
        }
    }
}
