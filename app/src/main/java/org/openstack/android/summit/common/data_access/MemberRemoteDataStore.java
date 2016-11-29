package org.openstack.android.summit.common.data_access;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.api.IMembersApi;
import org.openstack.android.summit.common.api.ISummitEventsApi;
import org.openstack.android.summit.common.api.ISummitExternalOrdersApi;
import org.openstack.android.summit.common.api.SummitEventFeedbackRequest;
import org.openstack.android.summit.common.api.SummitSelector;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.INonConfirmedSummitAttendeeDeserializer;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.NonConfirmedSummitAttendee;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.security.InvalidParameterException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Claudio Redi on 12/16/2015.
 */
public class MemberRemoteDataStore extends BaseRemoteDataStore implements IMemberRemoteDataStore {

    private IDeserializer                           deserializer;
    private INonConfirmedSummitAttendeeDeserializer nonConfirmedSummitAttendeeDeserializer;
    private Retrofit                                restClient;
    private IMembersApi                             memberApi;
    private ISummitEventsApi                        summitEventsApi;
    private ISummitExternalOrdersApi                summitExternalOrdersApi;

    @Inject
    public MemberRemoteDataStore
    (
        INonConfirmedSummitAttendeeDeserializer nonConfirmedSummitAttendeeDeserializer,
        IDeserializer deserializer,
        @Named("MemberProfile") Retrofit restClient
    )
    {
        this.nonConfirmedSummitAttendeeDeserializer = nonConfirmedSummitAttendeeDeserializer;
        this.deserializer                           = deserializer;
        this.restClient                             = restClient;
        this.memberApi                              = restClient.create(IMembersApi.class);
        this.summitEventsApi                        = restClient.create(ISummitEventsApi.class);
        this.summitExternalOrdersApi                = restClient.create(ISummitExternalOrdersApi.class);
    }

    @Override
    public void getMemberInfo(final IDataStoreOperationListener<Member> dataStoreOperationListener) {

        Call<ResponseBody> call = memberApi.info(SummitSelector.getCurrentSummitId(), "attendee,speaker,feedback");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    if(!response.isSuccessful())
                        throw new Exception("invalid http code!");

                    final String data = response.body().string();

                    if(data == null || data.isEmpty()) throw new InvalidParameterException("body is empty!");

                    Member member = RealmFactory.transaction(new RealmFactory.IRealmCallback<Member>() {
                        @Override
                        public Member callback(Realm session) throws Exception {
                            Member member = deserializer.deserialize(data, Member.class);
                            return session.copyToRealmOrUpdate(member);
                        }
                    });

                    dataStoreOperationListener.onSucceedWithSingleData(member);
                }
                catch (Exception e) {
                    Crashlytics.logException(e);
                    Log.e(Constants.LOG_TAG, "Error on member deserialization", e);
                    String friendlyError = Constants.GENERIC_ERROR_MSG;
                    dataStoreOperationListener.onError(friendlyError);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dataStoreOperationListener.onError(t.getMessage());
            }
        });
    }

    @Override
    public void getAttendeesForTicketOrder(String orderNumber, final IDataStoreOperationListener<NonConfirmedSummitAttendee> dataStoreOperationListener) {

        Call<ResponseBody> call = this.summitExternalOrdersApi.get(SummitSelector.getCurrentSummitId(), orderNumber.trim());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try
                {
                    if(!response.isSuccessful())
                    {
                        switch (response.code()){
                            case 412:
                                dataStoreOperationListener.onError
                                (
                                    OpenStackSummitApplication.context.getString(R.string.redeem_external_order_already_done_error)
                                );
                                return;
                            case 404:
                                dataStoreOperationListener.onError
                                (
                                    OpenStackSummitApplication.context.getString(R.string.redeem_external_order_not_found_error)
                                );
                                return;
                            default:
                                throw new Exception
                                        (
                                                String.format
                                                (
                                                    "getAttendeesForTicketOrder: http error %d",
                                                    response.code()
                                                )
                                        );
                        }
                    }

                    List<NonConfirmedSummitAttendee> nonConfirmedSummitAttendeeList = nonConfirmedSummitAttendeeDeserializer.deserializeArray(response.body().string());
                    dataStoreOperationListener.onSucceedWithDataCollection(nonConfirmedSummitAttendeeList);
                }
                catch (Exception ex){
                    Crashlytics.logException(ex);
                    Log.e(Constants.LOG_TAG, ex.getMessage(), ex);
                    String friendlyError = Constants.GENERIC_ERROR_MSG;
                    dataStoreOperationListener.onError(friendlyError);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dataStoreOperationListener.onError(t.getMessage());
            }
        });
    }

    public void selectAttendeeFromOrderList
    (
        String orderNumber,
        int externalAttendeeId,
        final IDataStoreOperationListener<NonConfirmedSummitAttendee> dataStoreOperationListener
    )
    {

        Call<ResponseBody> call = this.summitExternalOrdersApi.confirm(SummitSelector.getCurrentSummitId(), orderNumber.trim(), externalAttendeeId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{

                    if(!response.isSuccessful())
                    {
                        switch (response.code()){
                            case 412:
                                dataStoreOperationListener.onError
                                (
                                    OpenStackSummitApplication.context.getString(R.string.redeem_external_order_already_done_error)
                                );
                                return;
                            case 404:
                                dataStoreOperationListener.onError
                                (
                                    OpenStackSummitApplication.context.getString(R.string.redeem_external_order_attendee_does_not_exists_error)
                                );
                                return;
                            default:
                                throw new Exception
                                        (
                                                String.format
                                                        (
                                                                "selectAttendeeFromOrderList: http error %d",
                                                                response.code()
                                                        )
                                        );
                        }
                    }

                    dataStoreOperationListener.onSucceedWithoutData();
                }
                catch (Exception ex){
                    String friendlyError = Constants.GENERIC_ERROR_MSG;
                    Crashlytics.logException(ex);
                    dataStoreOperationListener.onError(friendlyError);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String friendlyError = Constants.GENERIC_ERROR_MSG;
                Crashlytics.logException(t);
                dataStoreOperationListener.onError(friendlyError);
            }
        });
    }

    @Override
    public void addFeedback(final Feedback feedback, final IDataStoreOperationListener<Feedback> dataStoreOperationListener) {

        int eventId             = feedback.getEvent().getId();
        Call<ResponseBody> call = this.summitEventsApi.postEventFeedback
        (
            "7",
            eventId,
            new SummitEventFeedbackRequest
            (
                feedback.getRate(),
                feedback.getReview()
            )
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{

                    if(!response.isSuccessful())
                        throw new Exception(String.format("addFeedback: http error code %d", response.code()));

                    feedback.setId(Integer.parseInt(response.body().string()));
                    dataStoreOperationListener.onSucceedWithSingleData(feedback);

                }
                catch (Exception ex){
                    Crashlytics.logException(ex);
                    String friendlyError = Constants.GENERIC_ERROR_MSG;
                    dataStoreOperationListener.onError(friendlyError);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Crashlytics.logException(t);
                String friendlyError = Constants.GENERIC_ERROR_MSG;
                dataStoreOperationListener.onError(friendlyError);
            }
        });
    }
}
