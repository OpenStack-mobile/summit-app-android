package org.openstack.android.summit.common.data_access.repositories.impl;

import org.openstack.android.summit.common.data_access.repositories.ITeamPushNotificationDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.notifications.TeamPushNotification;

public class TeamPushNotificationDataStore extends GenericDataStore<TeamPushNotification> implements ITeamPushNotificationDataStore {

    public TeamPushNotificationDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy, IDeleteStrategy deleteStrategy) {
        super(TeamPushNotification.class, saveOrUpdateStrategy, deleteStrategy);
    }
}
