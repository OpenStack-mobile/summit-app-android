package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Summit;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public interface ISummitRemoteDataStore {
    void getActive(IDataStoreOperationListener<Summit> dataStoreOperationListener);
}
