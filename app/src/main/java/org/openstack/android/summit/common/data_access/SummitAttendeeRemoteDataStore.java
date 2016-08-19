package org.openstack.android.summit.common.data_access;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.github.kevinsawicki.http.HttpRequest;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.api_endpoints.ApiEndpointBuilder;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializer;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.network.HttpTask;
import org.openstack.android.summit.common.network.HttpTaskListener;
import org.openstack.android.summit.common.network.IHttpTaskFactory;
import org.openstack.android.summit.common.security.AccountType;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/5/2016.
 */
public class SummitAttendeeRemoteDataStore extends BaseRemoteDataStore implements ISummitAttendeeRemoteDataStore {

    private IDeserializer deserializer;
    private IHttpTaskFactory httpTaskFactory;

    @Inject
    public SummitAttendeeRemoteDataStore(IHttpTaskFactory httpTaskFactory, IDeserializer deserializer) {
        this.httpTaskFactory = httpTaskFactory;
        this.deserializer    = deserializer;
    }

    @Override
    public void addEventToSchedule(SummitAttendee summitAttendee, SummitEvent summitEvent, IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener) {
        addOrRemoveEventFromSchedule(summitAttendee, summitEvent, dataStoreOperationListener, HttpRequest.METHOD_POST);
    }

    @Override
    public void removeEventFromSchedule(SummitAttendee summitAttendee, SummitEvent summitEvent, IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener) {
        addOrRemoveEventFromSchedule(summitAttendee, summitEvent, dataStoreOperationListener, HttpRequest.METHOD_DELETE);
    }

   private void addOrRemoveEventFromSchedule(final SummitAttendee summitAttendee, SummitEvent summitEvent, final IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener, String httpMethod) {
        HttpTaskListener httpTaskListener = new HttpTaskListener() {
            @Override
            public void onSucceed(String data) {
                dataStoreOperationListener.onSucceedWithSingleData(summitAttendee);
            }

            @Override
            public void onError(Throwable error) {
                String friendlyError = Constants.GENERIC_ERROR_MSG;
                dataStoreOperationListener.onError(friendlyError);
            }
        };

        HashMap<String, Object> params = new HashMap<>();
        params.put(ApiEndpointBuilder.EventIdParam, summitEvent.getId());

        HttpTask httpTask = null;
        try {
            httpTask = httpTaskFactory.create
            (
                AccountType.OIDC,
                ApiEndpointBuilder.getInstance().buildEndpoint
                (
                    getBaseResourceServerUrl(),
                    "current",
                    ApiEndpointBuilder.EndpointType.RemoveAddFromMySchedule,
                    params
                ).toString()
                ,
                httpMethod,
                null,
                httpMethod,
                httpTaskListener
            );
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            dataStoreOperationListener.onError(e.getMessage());
        }
        httpTask.execute();
    }
}
