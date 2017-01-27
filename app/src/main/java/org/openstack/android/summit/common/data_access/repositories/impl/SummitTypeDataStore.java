package org.openstack.android.summit.common.data_access.repositories.impl;

import org.openstack.android.summit.common.data_access.repositories.ISummitTypeDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.SummitType;

/**
 * Created by smarcet on 1/25/17.
 */

public class SummitTypeDataStore extends GenericDataStore<SummitType> implements ISummitTypeDataStore {

    public SummitTypeDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy, IDeleteStrategy deleteStrategy) {
        super(SummitType.class, saveOrUpdateStrategy, deleteStrategy);
    }
}
