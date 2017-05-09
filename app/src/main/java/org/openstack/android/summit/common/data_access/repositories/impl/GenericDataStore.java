package org.openstack.android.summit.common.data_access.repositories.impl;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.DataAccessException;
import org.openstack.android.summit.common.data_access.IDataStoreOperationListener;
import org.openstack.android.summit.common.data_access.repositories.IGenericDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public class GenericDataStore<T extends RealmObject> implements IGenericDataStore<T> {

    protected final Class<T> type;

    protected ISaveOrUpdateStrategy saveOrUpdateStrategy;

    protected IDeleteStrategy deleteStrategy;

    public GenericDataStore(Class<T> type, ISaveOrUpdateStrategy saveOrUpdateStrategy, IDeleteStrategy deleteStrategy) {
        this.type                 = type;
        this.saveOrUpdateStrategy = saveOrUpdateStrategy;
        this.deleteStrategy       = deleteStrategy;
    }

    public Class<T> getType() {
        return this.type;
    }

    @Override
    public T getById(int id) {
        try {
            return RealmFactory.transaction(session -> session.where(getType()).equalTo("id", id).findFirst());
        } catch (DataAccessException ex) {
            Log.e(Constants.LOG_TAG, ex.getMessage());
            Crashlytics.logException(ex);
        }
        return null;
    }

    @Override
    public List<T> getAll() {

        try {
            return  RealmFactory.transaction(session -> session.where(getType()).findAll());
        } catch (DataAccessException ex) {
            Log.e(Constants.LOG_TAG, ex.getMessage());
            Crashlytics.logException(ex);
        }
        return null;
    }

    @Override
    public List<T> getAll(String fieldNames[], Sort sortOrders[]) {

        try {
            return  RealmFactory.transaction(session -> {
                RealmResults<T> result = RealmFactory.getSession().where(getType()).findAll();
                return result.sort(fieldNames, sortOrders);
            });
        } catch (DataAccessException ex) {
            Log.e(Constants.LOG_TAG, ex.getMessage());
            Crashlytics.logException(ex);
        }
        return null;

    }

    @Override
    public void saveOrUpdate(final T entity, final IDataStoreOperationListener<T> callback) {
       this.saveOrUpdateStrategy.saveOrUpdate(entity, getType(), callback);
    }

    @Override
    public void saveOrUpdate(final T entity) {
        saveOrUpdate(entity, null);
    }

    @Override
    public void delete(final int id, final IDataStoreOperationListener<T> callback) {
       this.deleteStrategy.delete(id, getType(), callback);
    }

    @Override
    public void delete(final int id) {
        delete(id, null);
    }

    @Override
    public void clearDataLocal() {
        try{
            RealmFactory.transaction(session -> {
            session.deleteAll();
            return Void.getInstance();
            });
        }
        catch (Exception ex) {
            Log.e(Constants.LOG_TAG, ex.getMessage());
            Crashlytics.logException(ex);
        }
    }
}
