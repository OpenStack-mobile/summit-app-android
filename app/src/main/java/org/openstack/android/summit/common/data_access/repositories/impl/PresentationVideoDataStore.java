package org.openstack.android.summit.common.data_access.repositories.impl;

import org.openstack.android.summit.common.data_access.repositories.IPresentationVideoDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.PresentationVideo;

/**
 * Created by smarcet on 1/26/17.
 */

public class PresentationVideoDataStore extends GenericDataStore<PresentationVideo> implements IPresentationVideoDataStore {

    public PresentationVideoDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy, IDeleteStrategy deleteStrategy) {
        super(PresentationVideo.class, saveOrUpdateStrategy, deleteStrategy);
    }
}
