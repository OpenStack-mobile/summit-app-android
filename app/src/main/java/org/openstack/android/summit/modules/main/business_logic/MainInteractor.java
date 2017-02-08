package org.openstack.android.summit.modules.main.business_logic;

import android.util.Log;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.MemberDTO;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.repositories.IPushNotificationDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.data_polling.DataUpdatePoller;
import org.openstack.android.summit.common.entities.Member;
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
    private IReachability reachability;
    private IPushNotificationDataStore pushNotificationDataStore;
    private ISession session;

    public MainInteractor
    (
        ISummitDataStore summitDataStore,
        ISecurityManager securityManager,
        IPushNotificationsManager pushNotificationsManager,
        IDTOAssembler dtoAssembler,
        IReachability reachability,
        IPushNotificationDataStore pushNotificationDataStore,
        ISession session,
        ISummitSelector summitSelector
    )
    {
        super(dtoAssembler, summitSelector, summitDataStore);
        this.securityManager           = securityManager;
        this.pushNotificationsManager  = pushNotificationsManager;
        this.reachability              = reachability;
        this.pushNotificationDataStore = pushNotificationDataStore;
        this.session                   = session;
    }

    private final static String BuildNumber = "build-number";

    @Override
    public void setInstalledBuildNumber(int buildNumber){
        session.setInt(BuildNumber, buildNumber);
    }

    @Override
    public int getInstalledBuildNumber(){
        return session.getInt(BuildNumber);
    }

    @Override
    public void upgradeStorage() {
        // clear data update state
        DataUpdatePoller.clearState(session);
        if(securityManager.isLoggedIn()){
            securityManager.logout(false);
        }
        summitDataStore.clearDataLocal();
    }

    @Override
    public MemberDTO getCurrentMember() {
        Member member = securityManager.getCurrentMember();
        if(member == null) return null;
        return dtoAssembler.createDTO(member, MemberDTO.class);
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
            ArrayList<Integer> scheduleEventsIds = loggedInMember.getAttendeeRole() != null ? loggedInMember.getAttendeeRole().getScheduleEventIds(): null;
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