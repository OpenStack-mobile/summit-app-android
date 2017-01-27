package org.openstack.android.summit.common.data_access.repositories;

import org.openstack.android.summit.common.data_access.IDataStoreOperationListener;

import java.util.List;

import io.realm.RealmObject;
import io.realm.Sort;

/**
 * Created by Claudio Redi on 1/5/2016.
 */
public interface IGenericDataStore<T extends RealmObject> {

    T getById(int id);

    List<T> getAll();

    List<T> getAll(String fieldNames[], Sort sortOrders[]);

    void saveOrUpdate(T entity, IDataStoreOperationListener<T> delegate);

    void saveOrUpdate(T entity);

    void delete(int id);

    void delete(int id, IDataStoreOperationListener<T> delegate);

    void clearDataLocal();
}
