package org.openstack.android.summit.common.data_access;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.api.IMembersApi;
import org.openstack.android.summit.common.api.ISummitEventsApi;
import org.openstack.android.summit.common.api.ISummitExternalOrdersApi;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.api.SummitEventFeedbackRequest;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.INonConfirmedSummitAttendeeDeserializer;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.NonConfirmedSummitAttendee;
import org.openstack.android.summit.common.entities.exceptions.NotFoundEntityException;
import org.openstack.android.summit.common.entities.exceptions.ValidationException;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by Claudio Redi on 12/16/2015.
 */
public class MemberRemoteDataStore extends BaseRemoteDataStore implements IMemberRemoteDataStore {

    private IDeserializer deserializer;
    private INonConfirmedSummitAttendeeDeserializer nonConfirmedSummitAttendeeDeserializer;
    private IMembersApi memberApi;
    private ISummitEventsApi summitEventsApi;
    private ISummitExternalOrdersApi summitExternalOrdersApi;
    private ISummitSelector summitSelector;

    @Inject
    public MemberRemoteDataStore
    (
        INonConfirmedSummitAttendeeDeserializer nonConfirmedSummitAttendeeDeserializer,
        IDeserializer deserializer,
        @Named("MemberProfileRXJava2") Retrofit restClientRxJava,
        ISummitSelector summitSelector
    )
    {
        this.nonConfirmedSummitAttendeeDeserializer = nonConfirmedSummitAttendeeDeserializer;
        this.deserializer                           = deserializer;
        this.memberApi                              = restClientRxJava.create(IMembersApi.class);
        this.summitEventsApi                        = restClientRxJava.create(ISummitEventsApi.class);
        this.summitExternalOrdersApi                = restClientRxJava.create(ISummitExternalOrdersApi.class);
        this.summitSelector                         = summitSelector;
    }

    @Override
    public Observable<Member> getMemberInfo() {

        return memberApi.info(summitSelector.getCurrentSummitId(), "attendee,speaker,feedback")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> {
                    Member member = null;
                    int code      = response.code();
                    try {
                        if(!response.isSuccessful()){
                            throw new Exception(String.format("MemberRemoteDataStore.getMemberInfo http code %d", code));
                        }

                        final String data = response.body().string();

                        member = RealmFactory.transaction(session ->
                            session.copyToRealmOrUpdate(deserializer.deserialize(data, Member.class))
                        );
                    } catch (Exception ex) {
                        Crashlytics.logException(ex);
                        Crashlytics.log(String.format("MemberRemoteDataStore.getMemberInfo http code %d", code));
                        Log.e(Constants.LOG_TAG, ex.getMessage(), ex);
                        throw Exceptions.propagate(ex);
                    }
                    return member;
                });
    }

    @Override
    public Observable<List<NonConfirmedSummitAttendee>>  getAttendeesForTicketOrder(String orderNumber) {
        return summitExternalOrdersApi.get(summitSelector.getCurrentSummitId(), orderNumber.trim())
               .subscribeOn(Schedulers.io())
               .map(response -> {

                   if (!response.isSuccessful()) {
                       switch (response.code()) {
                           case 412:
                               throw new ValidationException(OpenStackSummitApplication.context.getString(R.string.redeem_external_order_already_done_error));
                           case 404:
                               throw new NotFoundEntityException( OpenStackSummitApplication.context.getString(R.string.redeem_external_order_not_found_error));
                       }
                       throw new Exception(String.format("getAttendeesForTicketOrder: http error code %d", response.code()));
                   }

                   return nonConfirmedSummitAttendeeDeserializer.deserializeArray(response.body().string());
                });
    }

    @Override
    public Observable<Boolean> selectAttendeeFromOrderList
    (
        String orderNumber,
        int externalAttendeeId
    )
    {

        return summitExternalOrdersApi.confirm(summitSelector.getCurrentSummitId(), orderNumber.trim(), externalAttendeeId)
               .subscribeOn(Schedulers.io())
                .map(response -> {

                    if (!response.isSuccessful()) {
                        switch (response.code()) {
                            case 412:
                                throw new ValidationException(OpenStackSummitApplication.context.getString(R.string.redeem_external_order_already_done_error));
                            case 404:
                                throw new NotFoundEntityException(OpenStackSummitApplication.context.getString(R.string.redeem_external_order_attendee_does_not_exists_error));
                        }
                        throw new Exception(String.format("getAttendeesForTicketOrder: http error code %d", response.code()));
                    }
                    return true;
                });


    }

    @Override
    public Observable<Integer> addFeedback(int eventId, int rate, String review) {

        return summitEventsApi.postEventFeedback
                (
                        summitSelector.getCurrentSummitId(),
                        eventId,
                        new SummitEventFeedbackRequest
                                (
                                        rate,
                                        review.trim()
                                )
                )
                .subscribeOn(Schedulers.io())
                .map(response -> {

                    if (!response.isSuccessful()) {
                        switch (response.code()) {
                            case 412:
                                throw new ValidationException("you already sent feedback for event");
                            case 404:
                                throw new NotFoundEntityException
                                        (
                                                String.format
                                                        (
                                                                OpenStackSummitApplication.context.getString(R.string.error_event_not_found),
                                                                eventId
                                                        )
                                        );
                        }
                        throw new Exception(String.format("addFeedback: http error code %d", response.code()));
                    }
                    return Integer.parseInt(response.body().string());
                });
    }

    @Override
    public Observable<Boolean> addSummitEvent2Favorites(int summitId, int eventId) {
        return memberApi.addToFavorites(summitId, eventId)
                .subscribeOn(Schedulers.io())
                .map(response -> {
                    if (!response.isSuccessful()) {
                        switch (response.code()) {
                            case 412:
                                throw new ValidationException
                                        (
                                                String.format
                                                        (
                                                                OpenStackSummitApplication.context.getString(R.string.error_already_in_favorites),
                                                                eventId
                                                        )
                                        );
                            case 404:

                                throw new NotFoundEntityException
                                        (
                                                String.format
                                                        (
                                                                OpenStackSummitApplication.context.getString(R.string.error_event_not_found),
                                                                eventId
                                                        )
                                        );
                            default:

                                throw new Exception
                                        (
                                                String.format
                                                        (
                                                                "addSummitEvent2Favorites: http error %d",
                                                                response.code()
                                                        )
                                        );
                        }
                    }
                    return true;
                });
    }

    @Override
    public Observable<Boolean> removeSummitEventFromFavorites(int summitId, int eventId) {
        return memberApi.removeFromFavorites(summitId, eventId)
                .subscribeOn(Schedulers.io())
                .map(response -> {
                    if (!response.isSuccessful()) {
                        switch (response.code()) {
                            case 412:
                                new ValidationException
                                        (
                                                String.format
                                                        (
                                                                OpenStackSummitApplication.context.getString(R.string.error_not_in_favorites),
                                                                eventId
                                                        )
                                        );
                            case 404:

                                throw new NotFoundEntityException
                                        (
                                                String.format
                                                        (
                                                                OpenStackSummitApplication.context.getString(R.string.error_event_not_found),
                                                                eventId
                                                        )
                                        );
                            default:

                                new Exception
                                        (
                                                String.format
                                                        (
                                                                "removeSummitEventFromFavorites: http error %d",
                                                                response.code()
                                                        )
                                        );
                        }
                    }
                    return true;
                });
    }
}
