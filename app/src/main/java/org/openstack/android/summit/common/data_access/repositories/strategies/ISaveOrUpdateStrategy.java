package org.openstack.android.summit.common.data_access.repositories.strategies;

import org.openstack.android.summit.common.data_access.IDataStoreOperationListener;

import io.realm.RealmObject;

/**
 * Created by smarcet on 1/25/17.
 */

public interface ISaveOrUpdateStrategy {

    <T extends RealmObject> void saveOrUpdate(final T entity, final Class<T> type, final IDataStoreOperationListener<T> callback);

    <T extends RealmObject> void saveOrUpdate(final T entity, final Class<T> type);
}
