package org.openstack.android.summit.modules.push_notifications_inbox.user_interface;

import org.openstack.android.summit.common.IBaseWireframe;
import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by sebastian on 8/22/2016.
 */
public interface IPushNotificationDetailWireframe extends IBaseWireframe {

    void showEventDetail(int eventId,  IBaseView context);
}
