package org.openstack.android.summit.modules.main.business_logic;

import android.net.Uri;

import org.openstack.android.summit.common.DTOs.MemberDTO;
import org.openstack.android.summit.common.business_logic.IBaseInteractor;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public interface IMainInteractor extends IBaseInteractor {
    String getCurrentMemberName();

    Uri getCurrentMemberProfilePictureUri();

    void subscribeLoggedInMemberToPushNotifications();

    void subscribeAnonymousToPushNotifications();

    boolean isDataLoaded();

    boolean isNetworkingAvailable();
}
