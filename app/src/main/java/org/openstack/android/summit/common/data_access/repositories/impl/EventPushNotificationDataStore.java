package org.openstack.android.summit.common.data_access.repositories.impl;

import org.openstack.android.summit.common.data_access.repositories.IEventPushNotificationDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.notifications.EventPushNotification;

/**
 * Created by smarcet on 1/25/17.
 */

public class EventPushNotificationDataStore extends GenericDataStore<EventPushNotification> implements IEventPushNotificationDataStore {

    public EventPushNotificationDataStore( ISaveOrUpdateStrategy saveOrUpdateStrategy, IDeleteStrategy deleteStrategy) {
        super(EventPushNotification.class, saveOrUpdateStrategy, deleteStrategy);
    }

}
