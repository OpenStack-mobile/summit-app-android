package org.openstack.android.summit.modules.push_notifications_inbox.user_interface;

import org.openstack.android.summit.common.user_interface.IBasePresenter;

/**
 * Created by sebastian on 8/22/2016.
 */
public interface IPushNotificationDetailPresenter extends IBasePresenter<IPushNotificationDetailView> {

    void onRemovePushNotification();

    void go2Event();
}
