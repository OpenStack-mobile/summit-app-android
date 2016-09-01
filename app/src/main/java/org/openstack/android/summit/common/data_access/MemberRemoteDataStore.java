package org.openstack.android.summit.common.data_access;

import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.github.kevinsawicki.http.HttpRequest;
import org.json.JSONObject;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.api_endpoints.ApiEndpointBuilder;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.INonConfirmedSummitAttendeeDeserializer;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.NonConfirmedSummitAttendee;
import org.openstack.android.summit.common.network.HttpTask;
import org.openstack.android.summit.common.network.HttpTaskListener;
import org.openstack.android.summit.common.network.IHttpTaskFactory;
import org.openstack.android.summit.common.security.AccountType;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.security.spec.InvalidParameterSpecException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.realm.Realm;

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
        this.httpTaskFactory                        = httpTaskFactory;
        this.deserializer                           = deserializer;
    }

    @Override
    public void getMemberInfo(final IDataStoreOperationListener<Member> dataStoreOperationListener) {

        HttpTaskListener httpTaskListener = new HttpTaskListener() {
            @Override
            public void onSucceed(String data) {
                try {
                    Member member = deserializer.deserialize(data, Member.class);
                    dataStoreOperationListener.onSucceedWithSingleData(member);
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Log.e(Constants.LOG_TAG, "Error on member deserialization", e);
                    String friendlyError = Constants.GENERIC_ERROR_MSG;
                    dataStoreOperationListener.onError(friendlyError);
                }
            }

            @Override
            public void onError(Throwable error) {
                dataStoreOperationListener.onError(error.getMessage());
            }
        };

        HashMap<String,Object> params = new HashMap<>();
        params.put(ApiEndpointBuilder.ExpandParam, "attendee,speaker,feedback");

        HttpTask httpTask = null;
        try {
            httpTask = httpTaskFactory.create
                    (
                            AccountType.OIDC,
                            ApiEndpointBuilder.getInstance().buildEndpoint
                            (
                                getBaseResourceServerUrl(),
                                "current",
                                ApiEndpointBuilder.EndpointType.GetMemberInfo,
                                params
                            ).toString(),
                            HttpRequest.METHOD_GET,
                            null,
                            null,
                            httpTaskListener
                    );
        }
        catch (InvalidParameterSpecException e) {
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

        Map<String,Object> params = new HashMap<>();
        params.put(ApiEndpointBuilder.OrderNumberParam, orderNumber.trim());

        HttpTask httpTask = null;
        try {
            httpTask = httpTaskFactory.create(
                    AccountType.OIDC,
                    ApiEndpointBuilder.getInstance().buildEndpoint(getBaseResourceServerUrl(), "current", ApiEndpointBuilder.EndpointType.GetExternalOrder, params).toString(),
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
                Crashlytics.logException(error);
                dataStoreOperationListener.onError(friendlyError);
            }
        };

        Map<String,Object> params = new HashMap<>();
        params.put(ApiEndpointBuilder.OrderNumberParam, orderNumber.trim());
        params.put(ApiEndpointBuilder.ExternalAttendeeIdParam, externalAttendeeId);

        HttpTask httpTask = null;
        try {
            httpTask = httpTaskFactory.create(
                    AccountType.OIDC,
                    ApiEndpointBuilder.getInstance().buildEndpoint(getBaseResourceServerUrl(), "current", ApiEndpointBuilder.EndpointType.ConfirmExternalOrder, params).toString(),
                    HttpRequest.METHOD_POST,
                    HttpRequest.CONTENT_TYPE_JSON,
                    null,
                    httpTaskListener);
            httpTask.execute();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
        }

    }

    @Override
    public void addFeedback(final Feedback feedback, final IDataStoreOperationListener<Feedback> dataStoreOperationListener) {

        HttpTaskListener httpTaskListener = new HttpTaskListener() {
            @Override
            public void onSucceed(String data) {
                feedback.setId(Integer.parseInt(data));
                dataStoreOperationListener.onSucceedWithSingleData(feedback);
            }

            @Override
            public void onError(Throwable error) {
                String friendlyError = Constants.GENERIC_ERROR_MSG;
                dataStoreOperationListener.onError(friendlyError);
            }
        };
        HttpTask httpTask         = null;
        Map<String,Object> params = new HashMap<>();
        params.put(ApiEndpointBuilder.EventIdParam, feedback.getEvent().getId());

        try {
            JSONObject feedbackJson = new JSONObject();
            feedbackJson.put("rate", feedback.getRate());
            feedbackJson.put("note", JSONObject.quote(feedback.getReview()));

            httpTask = httpTaskFactory.create(
                    AccountType.OIDC,
                    ApiEndpointBuilder.getInstance().buildEndpoint(getBaseResourceServerUrl(), "current", ApiEndpointBuilder.EndpointType.AddFeedback, params).toString(),
                    HttpRequest.METHOD_POST,
                    HttpRequest.CONTENT_TYPE_JSON,
                    feedbackJson.toString(),
                    httpTaskListener);
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
        }
        httpTask.execute();
    }

}
