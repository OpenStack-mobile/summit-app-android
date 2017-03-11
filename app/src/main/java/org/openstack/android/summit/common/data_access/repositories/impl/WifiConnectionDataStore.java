package org.openstack.android.summit.common.data_access.repositories.impl;

import org.openstack.android.summit.common.data_access.repositories.IWifiConnectionDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.SummitWIFIConnection;

/**
 * Created by smarcet on 1/26/17.
 */

public class WifiConnectionDataStore extends GenericDataStore<SummitWIFIConnection> implements IWifiConnectionDataStore {

    public WifiConnectionDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy, IDeleteStrategy deleteStrategy) {
        super(SummitWIFIConnection.class, saveOrUpdateStrategy, deleteStrategy);
    }
}
