package org.openstack.android.summit.common.data_access;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONException;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializer;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.network.HttpTask;
import org.openstack.android.summit.common.network.HttpTaskFactory;
import org.openstack.android.summit.common.network.HttpTaskListener;
import org.openstack.android.summit.common.network.IHttpTaskFactory;
import org.openstack.android.summit.common.security.AccountType;

import java.security.spec.InvalidParameterSpecException;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 12/16/2015.
 */
public class MemberRemoteDataStore extends BaseRemoteDataStore implements IMemberRemoteDataStore {
    private IDeserializer deserializer;
    private IHttpTaskFactory httpTaskFactory;

    @Inject
    public MemberRemoteDataStore(IHttpTaskFactory httpTaskFactory, IDeserializer deserializer) {
        this.httpTaskFactory = httpTaskFactory;
        this.deserializer = deserializer;
    }

    @Override
    public void getLoggedInMember(final IDataStoreOperationListener<Member> dataStoreOperationListener) {

        HttpTaskListener httpTaskListener = new HttpTaskListener() {
            @Override
            public void onSucceed(String data) {
                try {
                    Member member = deserializer.deserialize(data, Member.class);
                    dataStoreOperationListener.onSuceedWithSingleData(member);
                } catch (JSONException e) {
                    Crashlytics.logException(e);
                    Log.e(Constants.LOG_TAG, "Error deserializing member", e);
                    dataStoreOperationListener.onError(e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                dataStoreOperationListener.onError(error);
            }
        };

        String url = getResourceServerUrl() + "/api/v1/summits/current/attendees/me?expand=speaker,feedback";
        HttpTask httpTask = null;
        try {
            httpTask = httpTaskFactory.create(AccountType.OIDC, url, HttpRequest.METHOD_GET, null, null, httpTaskListener);
        } catch (InvalidParameterSpecException e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
        }
        httpTask.execute();
    }
}
