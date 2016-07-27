package org.openstack.android.summit.common.data_access;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONException;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializer;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.network.HttpTask;
import org.openstack.android.summit.common.network.HttpTaskListener;
import org.openstack.android.summit.common.network.IHttpTaskFactory;
import org.openstack.android.summit.common.security.AccountType;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public class SummitRemoteDataStore extends BaseRemoteDataStore implements ISummitRemoteDataStore {
    private IDeserializer deserializer;
    private IHttpTaskFactory httpTaskFactory;

    public SummitRemoteDataStore(IHttpTaskFactory httpTaskFactory, IDeserializer deserializer) {
        this.httpTaskFactory = httpTaskFactory;
        this.deserializer = deserializer;
    }

    @Override
    public void getActive(final IDataStoreOperationListener<Summit> dataStoreOperationListener) {
        try {
            HttpTaskListener httpTaskListener = new HttpTaskListener() {
                @Override
                public void onSucceed(String data) {
                    try {
                        Summit summit = deserializer.deserialize(data, Summit.class);
                        dataStoreOperationListener.onSucceedWithSingleData(summit);
                    } catch (JSONException e) {
                        Crashlytics.logException(e);
                        Log.e(Constants.LOG_TAG,"Error deserializing summit", e);
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
            String url = getBaseResourceServerUrl() + "/api/v1/summits/current?expand=locations,sponsors,summit_types,event_types,presentation_categories,schedule";
            HttpTask httpTask = httpTaskFactory.create(AccountType.ServiceAccount, url, HttpRequest.METHOD_GET, null, null, httpTaskListener);
            httpTask.execute();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            String friendlyError = Constants.GENERIC_ERROR_MSG;
            dataStoreOperationListener.onError(friendlyError);
        }
    }
}
