package org.openstack.android.summit.common.data_access;

import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public interface IDataStoreOperationListener<T extends RealmObject> {
    void onSuceedWithData(T data);
    void onSucceed();
    void onError(String message);
}

