package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.Summit;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public interface ISummitDataStore {
    void getActive();

    IDataStoreOperationListener<Summit> getDelegate();

    void setDelegate(IDataStoreOperationListener<Summit> delegate);
}
