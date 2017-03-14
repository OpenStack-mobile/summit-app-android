package org.openstack.android.summit.modules.main.business_logic;

import org.openstack.android.summit.common.DTOs.MemberDTO;
import org.openstack.android.summit.common.business_logic.IBaseInteractor;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public interface IMainInteractor extends IBaseInteractor {

    MemberDTO getCurrentMember();

    void subscribeToPushNotifications();

    boolean isDataLoaded();

    boolean isNetworkingAvailable();

    boolean isLoggedInAndConfirmedAttendee();

    boolean isMemberLogged();

    long getNotReadNotificationsCount();

    void unSubscribeToPushNotifications();
}
