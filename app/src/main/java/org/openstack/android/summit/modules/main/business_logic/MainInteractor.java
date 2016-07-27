package org.openstack.android.summit.modules.main.business_logic;

import android.net.Uri;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public class MainInteractor extends BaseInteractor implements IMainInteractor {
    private ISecurityManager securityManager;
    private IPushNotificationsManager pushNotificationsManager;
    private ISummitDataStore summitDataStore;
    private IReachability reachability;

    public MainInteractor(ISummitDataStore summitDataStore, ISecurityManager securityManager, IPushNotificationsManager pushNotificationsManager, IDTOAssembler dtoAssembler, IReachability reachability, IDataUpdatePoller dataUpdatePoller) {
        super(dtoAssembler, dataUpdatePoller);
        this.summitDataStore = summitDataStore;
        this.securityManager = securityManager;
        this.pushNotificationsManager = pushNotificationsManager;
        this.reachability = reachability;
    }

    @Override
    public String getCurrentMemberName() {
        Member member = securityManager.getCurrentMember();
        String fullName = "";
        if (member != null) {
            if (member.getSpeakerRole() != null) {
                fullName = member.getSpeakerRole().getFullName();
            }
            else if (member.getAttendeeRole() != null) {
                fullName = member.getAttendeeRole().getFullName();
            }
            else {
               fullName = member.getFullName();
            }
        }
        return fullName;
    }

    @Override
    public Uri getCurrentMemberProfilePictureUri() {
        Member member = securityManager.getCurrentMember();
        Uri uri = null;

        if (member != null) {
            String profilePicUrl = null;
            if (member.getSpeakerRole() != null) {
                profilePicUrl = member.getSpeakerRole().getPictureUrl();
            }
            else if (member.getAttendeeRole() != null) {
                profilePicUrl = member.getAttendeeRole().getPictureUrl();
            }
            else {
                profilePicUrl = member.getPictureUrl();
            }
            uri = profilePicUrl != null && !profilePicUrl.isEmpty() ? Uri.parse(profilePicUrl) : null;
        }

        return uri;
    }

    @Override
    public void subscribeLoggedInMemberToPushNotifications() {
        if (securityManager.isLoggedInAndConfirmedAttendee()) {
            Summit summit = summitDataStore.getActiveLocal();
            Member member = securityManager.getCurrentMember();
            pushNotificationsManager.subscribeMember(member, summit);
        }
    }

    @Override
    public void subscribeAnonymousToPushNotifications() {
        if (!securityManager.isLoggedIn()) {
            Summit summit = summitDataStore.getActiveLocal();
            pushNotificationsManager.subscribeAnonymous(summit);
        }
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
}