package org.openstack.android.summit.common.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.IMemberDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.Date;

import io.reactivex.Observable;

/**
 * Created by Claudio Redi on 1/4/2016.
 */
public class ScheduleableInteractor extends BaseInteractor implements IScheduleableInteractor {

    protected ISummitEventDataStore     summitEventDataStore;
    protected ISummitAttendeeDataStore  summitAttendeeDataStore;
    protected IPushNotificationsManager pushNotificationsManager;
    protected IMemberDataStore          memberDataStore;

    public ScheduleableInteractor
    (
            ISummitEventDataStore summitEventDataStore,
            ISummitAttendeeDataStore summitAttendeeDataStore,
            ISummitDataStore summitDataStore,
            IMemberDataStore memberDataStore,
            IDTOAssembler dtoAssembler,
            ISecurityManager securityManager,
            IPushNotificationsManager pushNotificationsManager,
            ISummitSelector summitSelector
    ) {
        super(securityManager, dtoAssembler, summitSelector, summitDataStore);

        this.summitEventDataStore     = summitEventDataStore;
        this.summitAttendeeDataStore  = summitAttendeeDataStore;
        this.memberDataStore          = memberDataStore;
        this.pushNotificationsManager = pushNotificationsManager;
    }

    @Override
    public Observable<Boolean> addEventToLoggedInMemberSchedule(int eventId)
    {

        if (!securityManager.isLoggedInAndConfirmedAttendee()) {
            return Observable.just(false);
        }

        final Member loggedInMember       = securityManager.getCurrentMember();

        final SummitEvent summitEvent     = summitEventDataStore.getById(eventId);
        if(summitEvent == null)
            return Observable.just(false);

        return summitAttendeeDataStore
                .addEventToMemberSchedule(loggedInMember.getAttendeeRole(), summitEvent)
                .doOnNext(res -> pushNotificationsManager.subscribeToEvent(eventId));
    }

    @Override
    public Observable<Boolean> removeEventFromLoggedInMemberSchedule(int eventId)
    {
        if (!this.isMemberLoggedInAndConfirmedAttendee()) {
            return Observable.just(false);
        }

        final Member loggedInMember       = securityManager.getCurrentMember();
        final SummitEvent summitEvent     = summitEventDataStore.getById(eventId);

        if(summitEvent == null)
            return Observable.just(false);

        return summitAttendeeDataStore
                .removeEventFromMemberSchedule(loggedInMember.getAttendeeRole(), summitEvent)
                .doOnNext(res -> pushNotificationsManager.unsubscribeFromEvent(eventId));
    }

    @Override
    public Observable<Boolean> deleteRSVP(int eventId)
    {
        if (!this.isMemberLoggedInAndConfirmedAttendee()) {
            return Observable.just(false);
        }

        final Member loggedInMember       = securityManager.getCurrentMember();
        final SummitEvent summitEvent     = summitEventDataStore.getById(eventId);

        if(summitEvent == null)
            return Observable.just(false);

        return summitAttendeeDataStore
                .deleteRSVP(loggedInMember.getAttendeeRole(), summitEvent)
                .doOnNext(res -> pushNotificationsManager.unsubscribeFromEvent(eventId));
    }


    @Override
    public boolean isEventScheduledByLoggedMember(int eventId) {

        if (!this.isMemberLoggedInAndConfirmedAttendee()) {
            return false;
        }

        Member loggedInMember = securityManager.getCurrentMember();

        if (loggedInMember == null || loggedInMember.getAttendeeRole() == null) {
            return false;
        }

        Boolean found = false;
        for (SummitEvent event : loggedInMember.getAttendeeRole().getScheduledEvents()) {
            if (event.getId() == eventId) {
                found = true;
                break;
            }
        }

        return found;
    }

    @Override
    public boolean isEventFavoriteByLoggedMember(int eventId) {
        if(!this.isMemberLoggedIn())
            return false;

        Member me = securityManager.getCurrentMember();

        final SummitEvent summitEvent = summitEventDataStore.getById(eventId);
        if(summitEvent == null)
            return false;

        return memberDataStore.isEventOnMyFavorites(me, summitEvent);

    }

    @Override
    public Observable<Boolean> addEventToMyFavorites(int eventId) {
        if(!this.isMemberLoggedIn())
            return Observable.just(false);

        final SummitEvent summitEvent = summitEventDataStore.getById(eventId);
        if(summitEvent == null)
            return Observable.just(false);

        return memberDataStore.addEventToMyFavorites(securityManager.getCurrentMember(), summitEvent);
    }

    @Override
    public Observable<Boolean> removeEventFromMemberFavorites(int eventId) {
        if(!this.isMemberLoggedIn())
            return Observable.just(false);

        final SummitEvent summitEvent = summitEventDataStore.getById(eventId);
        if(summitEvent == null)
            return Observable.just(false);

        return memberDataStore.removeEventFromMyFavorites(securityManager.getCurrentMember() , summitEvent);
    }

    public boolean shouldShowVenues() {
        Summit summit = summitDataStore.getById(summitSelector.getCurrentSummitId());

        return  summit != null &&
                summit.getStartShowingVenuesDate() != null &&
                summit.getStartShowingVenuesDate().getTime() < new Date().getTime();
    }
}