package org.openstack.android.summit.common.data_access;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.BuildConfig;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.INonConfirmedSummitAttendeeDeserializer;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.NonConfirmedSummitAttendee;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.network.HttpTask;
import org.openstack.android.summit.common.network.HttpTaskFactory;
import org.openstack.android.summit.common.network.HttpTaskListener;
import org.openstack.android.summit.common.network.IHttpTaskFactory;
import org.openstack.android.summit.common.security.AccountType;

import java.security.spec.InvalidParameterSpecException;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 12/16/2015.
 */
public class MemberRemoteDataStore extends BaseRemoteDataStore implements IMemberRemoteDataStore {

    private String userInfoEndpointUrl;

    public String getUserInfoEndpointUrl() {
        return userInfoEndpointUrl;
    }

    public void setUserInfoEndpointUrl(String userInfoEndpointUrl) {
        this.userInfoEndpointUrl = userInfoEndpointUrl;
    }

    private IDeserializer deserializer;
    private IHttpTaskFactory httpTaskFactory;
    private INonConfirmedSummitAttendeeDeserializer nonConfirmedSummitAttendeeDeserializer;

    @Inject
    public MemberRemoteDataStore(INonConfirmedSummitAttendeeDeserializer nonConfirmedSummitAttendeeDeserializer, IHttpTaskFactory httpTaskFactory, IDeserializer deserializer) {
        this.nonConfirmedSummitAttendeeDeserializer = nonConfirmedSummitAttendeeDeserializer;
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
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Log.e(Constants.LOG_TAG, "Error deserializing member", e);
                    String friendlyError = Constants.GENERIC_ERROR_MSG;
                    dataStoreOperationListener.onError(friendlyError);
                }
            }

            @Override
            public void onError(Throwable error) {
                dataStoreOperationListener.onError(error.getMessage());
            }
        };

        String url = getBaseResourceServerUrl() + "/api/v1/summits/current/attendees/me?expand=speaker,feedback";
        HttpTask httpTask = null;
        try {
            httpTask = httpTaskFactory.create(AccountType.OIDC, url, HttpRequest.METHOD_GET, null, null, httpTaskListener);
        } catch (InvalidParameterSpecException e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
        }
        httpTask.execute();
    }

    public void getLoggedInMemberBasicInfo(final IDataStoreOperationListener<Member> dataStoreOperationListener) {

        HttpTaskListener httpTaskListener = new HttpTaskListener() {
            @Override
            public void onSucceed(String data) {
                try {
                    Member member = deserializer.deserialize(data, Member.class);
                    dataStoreOperationListener.onSuceedWithSingleData(member);
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Log.e(Constants.LOG_TAG, "Error deserializing member", e);
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

        String url = getUserInfoEndpointUrl();

        HttpTask httpTask = null;
        try {
            httpTask = httpTaskFactory.create(AccountType.OIDC, url, HttpRequest.METHOD_GET, null, null, httpTaskListener);
        } catch (InvalidParameterSpecException e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
        }
        httpTask.execute();
    }

    @Override
    public void getAttendeesForTicketOrder(String orderNumber, final IDataStoreOperationListener<NonConfirmedSummitAttendee> dataStoreOperationListener) {
        HttpTaskListener httpTaskListener = new HttpTaskListener() {
            @Override
            public void onSucceed(String data) {
                try {
                    List<NonConfirmedSummitAttendee> nonConfirmedSummitAttendeeList = nonConfirmedSummitAttendeeDeserializer.deserializeArray(data);
                    dataStoreOperationListener.onSucceedWithDataCollection(nonConfirmedSummitAttendeeList);
                }
                catch (Exception e) {
                    Crashlytics.logException(e);
                    Log.e(Constants.LOG_TAG, e.getMessage(), e);
                    String friendlyError = Constants.GENERIC_ERROR_MSG;
                    dataStoreOperationListener.onError(friendlyError);
                }
            }

            @Override
            public void onError(Throwable error) {
                dataStoreOperationListener.onError(error.getMessage());
            }
        };

        String url = getBaseResourceServerUrl() +
                String.format("/api/v1/summits/current/external-orders/%s", orderNumber);
        HttpTask httpTask = null;
        try {
            httpTask = httpTaskFactory.create(
                    AccountType.OIDC,
                    url,
                    HttpRequest.METHOD_GET,
                    HttpRequest.CONTENT_TYPE_JSON,
                    null,
                    httpTaskListener);
        } catch (InvalidParameterSpecException e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
        }
        httpTask.execute();
    }

    public void selectAttendeeFromOrderList(String orderNumber, int externalAttendeeId, final IDataStoreOperationListener<NonConfirmedSummitAttendee> dataStoreOperationListener) {
        HttpTaskListener httpTaskListener = new HttpTaskListener() {
            @Override
            public void onSucceed(String data) {
                dataStoreOperationListener.onSucceedWithoutData();
            }

            @Override
            public void onError(Throwable error) {
                String friendlyError = Constants.GENERIC_ERROR_MSG;
                dataStoreOperationListener.onError(friendlyError);
            }
        };

        String url = getBaseResourceServerUrl() +
                String.format("/api/v1/summits/current/external-orders/%s/external-attendees/%d/confirm", orderNumber, externalAttendeeId);
        HttpTask httpTask = null;
        try {
            httpTask = httpTaskFactory.create(
                    AccountType.OIDC,
                    url,
                    HttpRequest.METHOD_POST,
                    HttpRequest.CONTENT_TYPE_JSON,
                    null,
                    httpTaskListener);
        } catch (InvalidParameterSpecException e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
        }
        httpTask.execute();
    }
}
