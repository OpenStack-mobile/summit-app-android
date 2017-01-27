package org.openstack.android.summit.common.entities.notifications;

import org.openstack.android.summit.common.entities.exceptions.NotFoundEntityException;

import java.util.Map;

/**
 * Created by smarcet on 1/24/17.
 */

public interface IPushNotificationFactory {

    IPushNotification build(Map<String, String> data) throws NotFoundEntityException;
}
