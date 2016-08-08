package org.openstack.android.summit.common.data_access;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public class GenericDataStore implements IGenericDataStore {

    @Override
    public <T extends RealmObject> T getByIdLocal(int id, Class<T> type) {
        return RealmFactory.getSession().where(type).equalTo("id", id).findFirst();
    }

    @Override
    public <T extends RealmObject> List<T> getAllLocal(Class<T> type) {
        RealmResults<T> result = RealmFactory.getSession().where(type).findAll();
        return result;
    }

    @Override
    public <T extends RealmObject> List<T> getAllLocal(Class<T> type, String fieldNames[], Sort sortOrders[]) {
        RealmResults<T> result = RealmFactory.getSession().where(type).findAll();
        result.sort(fieldNames, sortOrders);
        return result;
    }

    @Override
    public <T extends RealmObject> void saveOrUpdate(final T entity, IDataStoreOperationListener<T> delegate, Class<T> type) {
        T realmEntity;
        try{
            RealmFactory.getSession().beginTransaction();
            realmEntity = RealmFactory.getSession().copyToRealmOrUpdate(entity);
            RealmFactory.getSession().commitTransaction();
            if (delegate != null) {
                delegate.onSucceedWithSingleData(realmEntity);
            }
        }
        catch (Exception e) {
            RealmFactory.getSession().cancelTransaction();
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            if (delegate != null) {
                String friendlyError = Constants.GENERIC_ERROR_MSG;
                delegate.onError(friendlyError);
            }
        }
    }

    @Override
    public <T extends RealmObject> void delete(int id, IDataStoreOperationListener<T> delegate, Class<T> type) {
        try{
            RealmFactory.getSession().beginTransaction();
            RealmFactory.getSession().where(type).equalTo("id", id).findFirst().deleteFromRealm();
            RealmFactory.getSession().commitTransaction();
            if (delegate != null) {
                delegate.onSucceedWithoutData();
            }
        }
        catch (Exception e) {
            RealmFactory.getSession().cancelTransaction();
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            String friendlyError = Constants.GENERIC_ERROR_MSG;
            delegate.onError(friendlyError);
        }
    }

    @Override
    public void clearDataLocal() {
        RealmFactory.getSession().beginTransaction();
        try{
            RealmFactory.getSession().deleteAll();
            RealmFactory.getSession().commitTransaction();
        }
        catch (Exception e) {
            RealmFactory.getSession().cancelTransaction();
        }
    }
}
