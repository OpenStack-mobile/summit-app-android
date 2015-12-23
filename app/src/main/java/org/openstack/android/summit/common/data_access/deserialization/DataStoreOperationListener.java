package org.openstack.android.summit.common.data_access.deserialization;

import org.openstack.android.summit.common.data_access.IDataStoreOperationListener;

import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 12/22/2015.
 */
public abstract class DataStoreOperationListener<T extends RealmObject> implements IDataStoreOperationListener<T> {
    @Override
    public void onSuceedWithData(T data) {

    }

    @Override
    public void onSucceed() {

    }

    @Override
    public void onError(String message) {

    }
}
