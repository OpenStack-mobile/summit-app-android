package org.openstack.android.summit.modules.push_notifications_inbox.business_logic;

import org.openstack.android.summit.common.DTOs.PushNotificationDetailDTO;
import org.openstack.android.summit.common.DTOs.PushNotificationListItemDTO;
import org.openstack.android.summit.common.business_logic.IBaseInteractor;

/**
 * Created by sebastian on 8/22/2016.
 */
public interface IPushNotificationDetailInteractor  extends IBaseInteractor {

    PushNotificationDetailDTO getPushNotificationDetail(int pushNotificationId);

    void deleteNotification(PushNotificationListItemDTO notification);
}
