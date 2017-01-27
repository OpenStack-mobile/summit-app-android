package org.openstack.android.summit.common.data_access.repositories.impl;

import org.openstack.android.summit.common.data_access.repositories.IPresentationLinkDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.PresentationLink;

/**
 * Created by smarcet on 1/26/17.
 */

public class PresentationLinkDataStore extends GenericDataStore<PresentationLink> implements IPresentationLinkDataStore {

    public PresentationLinkDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy, IDeleteStrategy deleteStrategy) {
        super(PresentationLink.class, saveOrUpdateStrategy, deleteStrategy);
    }
}
