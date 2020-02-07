package org.openstack.android.summit.modules.main.business_logic;

import android.util.Log;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.EventDetailDTO;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.ISummitEventRemoteDataStore;
import org.openstack.android.summit.common.data_access.repositories.IPushNotificationDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public class MainInteractor extends BaseInteractor implements IMainInteractor {

    private IPushNotificationsManager pushNotificationsManager;
    private IPushNotificationDataStore pushNotificationDataStore;
    private ISession session;
    private ISummitEventDataStore summitEventDataStore;
    private ISummitEventRemoteDataStore summitEventRemoteDataStore;

    public MainInteractor
    (
        ISummitDataStore summitDataStore,
        ISummitEventDataStore summitEventDataStore,
        ISummitEventRemoteDataStore summitEventRemoteDataStore,
        ISecurityManager securityManager,
        IPushNotificationsManager pushNotificationsManager,
        IDTOAssembler dtoAssembler,
        IPushNotificationDataStore pushNotificationDataStore,
        ISession session,
        ISummitSelector summitSelector,
        IReachability reachability
    )
    {
        super(securityManager, dtoAssembler, summitSelector, summitDataStore, reachability);

        this.pushNotificationsManager   = pushNotificationsManager;
        this.pushNotificationDataStore  = pushNotificationDataStore;
        this.session                    = session;
        this.summitEventDataStore       = summitEventDataStore;
        this.summitEventRemoteDataStore = summitEventRemoteDataStore;
    }

    @Override
    public void subscribeToPushNotifications() {
        Log.d(Constants.LOG_TAG, "MainInteractor.subscribeToPushNotifications");
        SummitDTO summit = this.getActiveSummit();
        if(summit == null) return;
        int summitId = summit.getId();

        if (securityManager.isLoggedIn() && !pushNotificationsManager.isMemberSubscribed()){
            Member loggedInMember                = securityManager.getCurrentMember();
            int memberId                         = loggedInMember.getId();
            int speakerId                        = loggedInMember.getSpeakerRole()  != null ? loggedInMember.getSpeakerRole().getId() : 0;
            int attendeeId                       = loggedInMember.getAttendeeRole() != null ? loggedInMember.getAttendeeRole().getId() : 0;
            ArrayList<Integer> scheduleEventsIds = loggedInMember.getAttendeeRole() != null ? loggedInMember.getScheduleEventIds(): null;
            pushNotificationsManager.subscribeMember(memberId, summitId, speakerId, attendeeId, scheduleEventsIds);
            return;
        }

        if(!securityManager.isLoggedIn() && !pushNotificationsManager.isAnonymousSubscribed())
            pushNotificationsManager.subscribeAnonymous(summitId);
    }

    @Override
    public void unSubscribeToPushNotifications() {
        Log.d(Constants.LOG_TAG, "MainInteractor.unSubscribeToPushNotifications");
        pushNotificationsManager.unSubscribe();
    }

    @Override
    public Observable<EventDetailDTO> getEventById(int eventId) {
        SummitEvent event = summitEventDataStore.getById(eventId);
        if(event != null){
            return  Observable.just(dtoAssembler.createDTO(event, EventDetailDTO.class));
        }
        return summitEventRemoteDataStore.getSummitEventById(eventId).map( retrievedEvent -> dtoAssembler.createDTO(retrievedEvent, EventDetailDTO.class));
    }

    @Override
    public long getNotReadNotificationsCount() {
        return pushNotificationDataStore.getNotOpenedCountBy(securityManager.getCurrentMemberId());
    }
}