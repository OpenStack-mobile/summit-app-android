package org.openstack.android.summit.modules.push_notifications.business_logic;

import org.openstack.android.summit.common.business_logic.IBaseInteractor;
import org.openstack.android.summit.common.entities.notifications.IPushNotification;

/**
 * Created by smarcet on 1/24/17.
 */

public interface IPushNotificationInteractor extends IBaseInteractor {
    IPushNotification save(IPushNotification pushNotification);
}
