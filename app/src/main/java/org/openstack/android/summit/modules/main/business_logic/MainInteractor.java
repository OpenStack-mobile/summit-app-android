package org.openstack.android.summit.modules.main.business_logic;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.MemberDTO;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.IPushNotificationDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.ArrayList;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public class MainInteractor extends BaseInteractor implements IMainInteractor {

    private ISecurityManager securityManager;
    private IPushNotificationsManager pushNotificationsManager;
    private ISummitDataStore summitDataStore;
    private IReachability reachability;
    private IPushNotificationDataStore pushNotificationDataStore;

    public MainInteractor
    (
        ISummitDataStore summitDataStore,
        ISecurityManager securityManager,
        IPushNotificationsManager pushNotificationsManager,
        IDTOAssembler dtoAssembler,
        IReachability reachability,
        IPushNotificationDataStore pushNotificationDataStore
    )
    {
        super(dtoAssembler);
        this.summitDataStore           = summitDataStore;
        this.securityManager           = securityManager;
        this.pushNotificationsManager  = pushNotificationsManager;
        this.reachability              = reachability;
        this.pushNotificationDataStore = pushNotificationDataStore;
    }

    @Override
    public MemberDTO getCurrentMember() {
        Member member = securityManager.getCurrentMember();
        if(member == null) return null;
        return dtoAssembler.createDTO(member, MemberDTO.class);
    }

    @Override
    public void subscribeToPushNotifications() {
        Summit summit = summitDataStore.getActiveLocal();
        if(summit == null) return;
        int summitId = summit.getId();

        if (securityManager.isLoggedIn()){
            Member loggedInMember       = securityManager.getCurrentMember();
            int memberId                = loggedInMember.getId();
            int speakerId               = loggedInMember.getSpeakerRole()  != null ? loggedInMember.getSpeakerRole().getId() : 0;
            int attendeeId              = loggedInMember.getAttendeeRole() != null ? loggedInMember.getAttendeeRole().getId() : 0;
            ArrayList<Integer> scheduleEventsIds = loggedInMember.getAttendeeRole() != null ? loggedInMember.getAttendeeRole().getScheduleEventIds(): null;
            pushNotificationsManager.subscribeMember(memberId, summitId, speakerId, attendeeId, scheduleEventsIds);
            return;
        }

        pushNotificationsManager.subscribeAnonymous(summitId);
    }

    @Override
    public boolean isDataLoaded() {
        return summitDataStore.getActiveLocal() != null;
    }

    @Override
    public boolean isNetworkingAvailable() {
        return reachability.isNetworkingAvailable(OpenStackSummitApplication.context);
    }

    @Override
    public boolean isLoggedInAndConfirmedAttendee() {
        return securityManager.isLoggedInAndConfirmedAttendee();
    }

    @Override
    public boolean isMemberLogged() {
        return securityManager.isLoggedIn();
    }

    @Override
    public long getNotReadNotificationsCount() {
        return pushNotificationDataStore.getNotOpenedCountBy(securityManager.getCurrentMember());
    }
}