package org.openstack.android.openstacksummit.common.data_access;

import org.openstack.android.openstacksummit.common.entities.Summit;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public interface ISummitRemoteDataStore {
    void getActive(IDataStoreOperationListener<Summit> delegate);
}
