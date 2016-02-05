package org.openstack.android.summit.common.data_access;

import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONException;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializer;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.network.HttpTask;
import org.openstack.android.summit.common.network.HttpTaskListener;
import org.openstack.android.summit.common.network.IHttpTaskFactory;
import org.openstack.android.summit.common.security.AccountType;

import java.security.spec.InvalidParameterSpecException;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/5/2016.
 */
public class SummitAttendeeRemoteDataStore implements ISummitAttendeeRemoteDataStore {
    private IDeserializer deserializer;
    private IHttpTaskFactory httpTaskFactory;

    @Inject
    public SummitAttendeeRemoteDataStore(IHttpTaskFactory httpTaskFactory, IDeserializer deserializer) {
        this.httpTaskFactory = httpTaskFactory;
        this.deserializer = deserializer;
    }

    @Override
    public void addEventToShedule(SummitAttendee summitAttendee, SummitEvent summitEvent, IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener) {
        addOrRemoveEventFromSchedule(summitAttendee, summitEvent, dataStoreOperationListener, HttpRequest.METHOD_POST);
    }

    @Override
    public void removeEventFromShedule(SummitAttendee summitAttendee, SummitEvent summitEvent, IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener) {
        addOrRemoveEventFromSchedule(summitAttendee, summitEvent, dataStoreOperationListener, HttpRequest.METHOD_DELETE);
    }

    private void addOrRemoveEventFromSchedule(final SummitAttendee summitAttendee, SummitEvent summitEvent, final IDataStoreOperationListener<SummitAttendee> dataStoreOperationListener, String httpMethod) {
        HttpTaskListener httpTaskListener = new HttpTaskListener() {
            @Override
            public void onSucceed(String data) {
                dataStoreOperationListener.onSuceedWithSingleData(summitAttendee);
            }

            @Override
            public void onError(String error) {
                dataStoreOperationListener.onError(error);
            }
        };

        String url = Constants.RESOURCE_SERVER_BASE_URL +
                String.format("/api/v1/summits/current/attendees/%s/schedule/%s", summitAttendee.getId(), summitEvent.getId());
        HttpTask httpTask = null;
        try {
            httpTask = httpTaskFactory.Create(AccountType.OIDC, url, httpMethod, httpTaskListener);
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        }
        httpTask.execute();
    }
}
