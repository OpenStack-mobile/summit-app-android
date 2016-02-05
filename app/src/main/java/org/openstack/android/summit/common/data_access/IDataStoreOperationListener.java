package org.openstack.android.summit.common.data_access;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public interface IDataStoreOperationListener<T> {
    void onSucceedWithDataCollection(List<T> data);
    void onSuceedWithSingleData(T data);
    void onSucceedWithoutData();
    void onError(String message);
}

