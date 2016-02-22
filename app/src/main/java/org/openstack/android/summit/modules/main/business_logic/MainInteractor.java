package org.openstack.android.summit.modules.main.business_logic;

import android.net.Uri;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.IPushNotificationsManager;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.security.ISecurityManager;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public class MainInteractor extends BaseInteractor implements IMainInteractor {
    private ISecurityManager securityManager;
    private IPushNotificationsManager pushNotificationsManager;
    private ISummitDataStore summitDataStore;

    public MainInteractor(ISummitDataStore summitDataStore, ISecurityManager securityManager, IPushNotificationsManager pushNotificationsManager, IDTOAssembler dtoAssembler) {
        super(dtoAssembler);
        this.summitDataStore = summitDataStore;
        this.securityManager = securityManager;
        this.pushNotificationsManager = pushNotificationsManager;
    }

    @Override
    public String getCurrentMemberName() {
        Member member = securityManager.getCurrentMember();
        String fullName = member.getSpeakerRole() != null ? member.getSpeakerRole().getFullName() : member.getAttendeeRole().getFullName();
        return fullName;
    }

    @Override
    public Uri getCurrentMemberProfilePictureUri() {
        Member member = securityManager.getCurrentMember();
        String profilePicUrl = member.getSpeakerRole() != null ? member.getSpeakerRole().getPictureUrl() : member.getAttendeeRole().getPictureUrl();
        Uri uri = profilePicUrl != null && !profilePicUrl.isEmpty() ? Uri.parse(profilePicUrl) : null;
        return uri;
    }

    @Override
    public void subscribeLoggedInMemberToPushNotifications() {
        if (securityManager.isLoggedIn()) {
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
}