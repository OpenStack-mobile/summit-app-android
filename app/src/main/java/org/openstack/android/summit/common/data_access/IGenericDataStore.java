package org.openstack.android.summit.common.data_access;

import java.util.List;

import io.realm.RealmObject;
import io.realm.Sort;

/**
 * Created by Claudio Redi on 1/5/2016.
 */
public interface IGenericDataStore {
    <T extends RealmObject> T getByIdLocal(int id, Class<T> type);

    <T extends RealmObject> List<T> getAllLocal(Class<T> type);

    <T extends RealmObject> List<T> getAllLocal(Class<T> type, String fieldNames[], Sort sortOrders[]);

    <T extends RealmObject> void saveOrUpdate(T entity, IDataStoreOperationListener<T> delegate, Class<T> type);

    <T extends RealmObject> void delete(int id, IDataStoreOperationListener<T> delegate, Class<T> type);

    void clearDataLocal();
}
