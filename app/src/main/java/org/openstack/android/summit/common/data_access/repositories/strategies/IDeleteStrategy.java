package org.openstack.android.summit.common.data_access.repositories.strategies;

import org.openstack.android.summit.common.data_access.IDataStoreOperationListener;

import io.realm.RealmObject;

/**
 * Created by smarcet on 1/25/17.
 */

public interface IDeleteStrategy {

    <T extends RealmObject> void delete(final int id, final Class<T> type, final IDataStoreOperationListener<T> callback);

    <T extends RealmObject> void delete(final int id, final Class<T> type);
}
