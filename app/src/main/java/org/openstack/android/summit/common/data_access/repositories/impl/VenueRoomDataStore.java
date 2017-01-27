package org.openstack.android.summit.common.data_access.repositories.impl;

import org.openstack.android.summit.common.data_access.repositories.IVenueRoomDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.VenueRoom;

public class VenueRoomDataStore extends GenericDataStore<VenueRoom> implements IVenueRoomDataStore {

    public VenueRoomDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy, IDeleteStrategy deleteStrategy) {
        super(VenueRoom.class, saveOrUpdateStrategy, deleteStrategy);
    }
}
