package org.openstack.android.summit.common.data_access.repositories.impl;

import org.openstack.android.summit.common.data_access.repositories.ITagDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.Tag;

/**
 * Created by smarcet on 1/25/17.
 */

public class TagDataStore extends GenericDataStore<Tag> implements ITagDataStore {

    public TagDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy, IDeleteStrategy deleteStrategy) {
        super(Tag.class, saveOrUpdateStrategy, deleteStrategy);
    }
}
