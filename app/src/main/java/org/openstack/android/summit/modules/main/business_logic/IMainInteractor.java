package org.openstack.android.summit.modules.main.business_logic;

import org.openstack.android.summit.common.business_logic.IBaseInteractor;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public interface IMainInteractor extends IBaseInteractor {

    void subscribeToPushNotifications();

    boolean isDataLoaded();

    boolean isNetworkingAvailable();

    long getNotReadNotificationsCount();

    void setInstalledBuildNumber(int buildNumber);

    int getInstalledBuildNumber();

    void upgradeStorage();

    void unSubscribeToPushNotifications();
}
