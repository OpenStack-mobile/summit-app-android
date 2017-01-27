package org.openstack.android.summit.common.data_access.repositories.impl;

import org.openstack.android.summit.common.data_access.repositories.IImageDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.Image;

/**
 * Created by smarcet on 1/25/17.
 */

public class ImageDataStore extends GenericDataStore<Image> implements IImageDataStore {

    public ImageDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy, IDeleteStrategy deleteStrategy) {
        super(Image.class, saveOrUpdateStrategy, deleteStrategy);
    }
}
