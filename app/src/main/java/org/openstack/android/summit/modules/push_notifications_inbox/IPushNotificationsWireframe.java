package org.openstack.android.summit.modules.push_notifications_inbox;

import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by sebastian on 8/19/2016.
 */
public interface IPushNotificationsWireframe {

    void presentNotificationsListView(IBaseView context);

    void showNotification(int notificationId, IBaseView context);
}
