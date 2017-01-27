package org.openstack.android.summit.common.data_access.repositories.impl;

import org.openstack.android.summit.common.data_access.repositories.IPresentationDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.Presentation;

/**
 * Created by smarcet on 1/26/17.
 */

public class PresentationDataStore extends GenericDataStore<Presentation> implements IPresentationDataStore {

    public PresentationDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy, IDeleteStrategy deleteStrategy) {
        super(Presentation.class, saveOrUpdateStrategy, deleteStrategy);
    }
}
