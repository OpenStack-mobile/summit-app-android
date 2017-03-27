package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.api.IAttendeeAPI;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.exceptions.NotFoundEntityException;
import org.openstack.android.summit.common.entities.exceptions.ValidationException;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by Claudio Redi on 1/5/2016.
 */
public class SummitAttendeeRemoteDataStore
        extends BaseRemoteDataStore
        implements ISummitAttendeeRemoteDataStore {

    private Retrofit restClientRxJava;
    private IAttendeeAPI attendeeAPI;
    private ISummitSelector summitSelector;

    @Inject
    public SummitAttendeeRemoteDataStore
            (
                    @Named("MemberProfileRXJava2") Retrofit restClientRxJava,
                    ISummitSelector summitSelector
            ) {
        this.restClientRxJava = restClientRxJava;
        this.attendeeAPI = restClientRxJava.create(IAttendeeAPI.class);
        this.summitSelector = summitSelector;
    }

    @Override
    public Observable<Boolean> addEventToSchedule
            (
                    SummitAttendee summitAttendee,
                    SummitEvent summitEvent
            ) {
        final int eventId = summitEvent.getId();
        return attendeeAPI.addToMySchedule(summitEvent.getSummit().getId(), eventId)
                .subscribeOn(Schedulers.io())
                .map(response -> {
                    if (!response.isSuccessful()) {
                        switch (response.code()) {
                            case 412:
                                throw new ValidationException
                                        (
                                                String.format
                                                        (
                                                                OpenStackSummitApplication.context.getString(R.string.error_already_in_schedule),
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
                                                                "addEventToSchedule: http error %d",
                                                                response.code()
                                                        )
                                        );
                        }
                    }
                    return true;
                });
    }

    @Override
    public Observable<Boolean> removeEventFromSchedule
    (
        SummitAttendee summitAttendee,
        SummitEvent summitEvent
    )
    {

        final int eventId = summitEvent.getId();
        return attendeeAPI.removeFromMySchedule(summitEvent.getSummit().getId(), eventId)
                .subscribeOn(Schedulers.io())
                .map(response -> {
                    if (!response.isSuccessful()) {
                        switch (response.code()) {
                            case 412:
                                throw new ValidationException
                                        (
                                                String.format
                                                        (
                                                                OpenStackSummitApplication.context.getString(R.string.error_already_in_schedule),
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
                                                                "removeEventFromSchedule: http error %d",
                                                                response.code()
                                                        )
                                        );
                        }
                    }
                    return true;
                });
    }

    @Override
    public Observable<Boolean> deleteRSVP(SummitAttendee summitAttendee, SummitEvent summitEvent) {
        final int eventId = summitEvent.getId();
        return attendeeAPI.deleteRSVP(summitEvent.getSummit().getId(), eventId)
                .subscribeOn(Schedulers.io())
                .map(response -> {
                    if (!response.isSuccessful()) {
                        switch (response.code()) {
                            case 412:
                                throw new ValidationException
                                        (
                                                String.format
                                                        (
                                                                OpenStackSummitApplication.context.getString(R.string.error_already_in_schedule),
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
                                                                "removeEventFromSchedule: http error %d",
                                                                response.code()
                                                        )
                                        );
                        }
                    }
                    return true;
                });
    }

}
