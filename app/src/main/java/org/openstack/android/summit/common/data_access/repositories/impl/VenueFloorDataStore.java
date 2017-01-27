package org.openstack.android.summit.common.data_access.repositories.impl;

import org.openstack.android.summit.common.data_access.repositories.IVenueFloorDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.VenueFloor;

public class VenueFloorDataStore extends GenericDataStore<VenueFloor> implements IVenueFloorDataStore {

    public VenueFloorDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy, IDeleteStrategy deleteStrategy) {
        super(VenueFloor.class, saveOrUpdateStrategy, deleteStrategy);
    }
}
