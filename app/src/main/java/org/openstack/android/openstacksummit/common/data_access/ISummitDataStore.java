package org.openstack.android.openstacksummit.common.data_access;

import org.openstack.android.openstacksummit.common.entities.Summit;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public interface ISummitDataStore {
    void getActive(IDataStoreOperationListener<Summit> delegate);

    void onSuceedWithData(Summit data);

    void onSucceed();

    void onError(String message);
}
