package org.openstack.android.summit.common.data_access.repositories.impl;

import org.openstack.android.summit.common.data_access.repositories.IEventTypeDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.EventType;

/**
 * Created by smarcet on 1/25/17.
 */

public class EventTypeDataStore extends GenericDataStore<EventType> implements IEventTypeDataStore {

    public EventTypeDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy, IDeleteStrategy deleteStrategy) {
        super(EventType.class, saveOrUpdateStrategy, deleteStrategy);
    }

}
