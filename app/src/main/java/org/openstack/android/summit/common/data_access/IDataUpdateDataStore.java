package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.DataUpdate;

/**
 * Created by Claudio Redi on 2/7/2016.
 */
public interface IDataUpdateDataStore extends IGenericDataStore {
    int getLatestDataUpdate();
}
