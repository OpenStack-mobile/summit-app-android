package org.openstack.android.summit.common.data_access.deserialization;

import org.openstack.android.summit.common.data_access.IDataStoreOperationListener;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 12/22/2015.
 */
public abstract class DataStoreOperationListener<T extends RealmObject> implements IDataStoreOperationListener<T> {
    @Override
    public void onSucceedWithDataCollection(List<T> data) {

    }

    @Override
    public void onSuceedWithSingleData(T data) {

    }

    @Override
    public void onSucceedWithoutData() {

    }

    @Override
    public void onError(String message) {

    }
}
