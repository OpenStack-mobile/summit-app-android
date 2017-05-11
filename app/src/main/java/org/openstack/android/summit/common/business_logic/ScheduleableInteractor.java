package org.openstack.android.summit.common.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.IMemberDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.Date;
import java.util.List;

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

    public List<ScheduleItemDTO> postProcessScheduleEventList(List<ScheduleItemDTO> list){
        // set favorite/schedule
        int  memberId         =  securityManager.getCurrentMemberId();
        int currentAttendeeId = 0;

        if(memberId > 0){
            Member currentMember                 = securityManager.getCurrentMember();
            SummitAttendee currentSummitAttendee = currentMember.getAttendeeRole();
            currentAttendeeId                    = currentSummitAttendee != null ? currentSummitAttendee.getId() : 0;
        }

        for(ScheduleItemDTO item:list){
            item.setFavorite(memberId           > 0 ? memberDataStore.isEventOnMyFavorites(memberId, item.getId()): false);
            item.setScheduled(currentAttendeeId > 0 ? summitAttendeeDataStore.isEventScheduledByAttendee(currentAttendeeId, item.getId()): false);
        }
        return list;
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

        Member member = securityManager.getCurrentMember();

        if (member == null) {
            return false;
        }

        SummitAttendee attendee =  member.getAttendeeRole();

        if(attendee == null) return false;

        return summitAttendeeDataStore.isEventScheduledByAttendee(attendee.getId(), eventId);
    }

    @Override
    public boolean isEventFavoriteByLoggedMember(int eventId) {
        int memberId = securityManager.getCurrentMemberId();

        if(memberId <= 0) return false;

        return memberDataStore.isEventOnMyFavorites(memberId, eventId);
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