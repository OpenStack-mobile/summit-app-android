package org.openstack.android.summit.common.data_access.repositories.impl;

import org.openstack.android.summit.common.data_access.repositories.IPresentationSlideDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.PresentationSlide;

/**
 * Created by smarcet on 1/26/17.
 */

public class PresentationSlideDataStore extends GenericDataStore<PresentationSlide> implements IPresentationSlideDataStore {

    public PresentationSlideDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy, IDeleteStrategy deleteStrategy) {
        super(PresentationSlide.class, saveOrUpdateStrategy, deleteStrategy);
    }
}
