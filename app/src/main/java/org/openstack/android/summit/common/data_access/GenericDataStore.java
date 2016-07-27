package org.openstack.android.summit.common.data_access;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public class GenericDataStore implements IGenericDataStore {
    public Realm realm = Realm.getDefaultInstance();

    @Override
    public <T extends RealmObject> T getByIdLocal(int id, Class<T> type) {
        return realm.where(type).equalTo("id", id).findFirst();
    }

    @Override
    public <T extends RealmObject> List<T> getAllLocal(Class<T> type) {
        RealmResults<T> result = realm.where(type).findAll();
        return result;
    }

    @Override
    public <T extends RealmObject> List<T> getAllLocal(Class<T> type, String fieldNames[], Sort sortOrders[]) {
        RealmResults<T> result = realm.where(type).findAll();
        result.sort(fieldNames, sortOrders);
        return result;
    }

    @Override
    public <T extends RealmObject> void saveOrUpdate(final T entity, IDataStoreOperationListener<T> delegate, Class<T> type) {
        T realmEntity;
        try{
            realm.beginTransaction();
            realmEntity = realm.copyToRealmOrUpdate(entity);
            realm.commitTransaction();
            if (delegate != null) {
                delegate.onSucceedWithSingleData(realmEntity);
            }
        }
        catch (Exception e) {
            realm.cancelTransaction();
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
            realm.beginTransaction();
            realm.where(type).equalTo("id", id).findFirst().deleteFromRealm();
            realm.commitTransaction();
            if (delegate != null) {
                delegate.onSucceedWithoutData();
            }
        }
        catch (Exception e) {
            realm.cancelTransaction();
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            String friendlyError = Constants.GENERIC_ERROR_MSG;
            delegate.onError(friendlyError);
        }
    }

    @Override
    public void clearDataLocal() {
        realm.beginTransaction();
        try{
            realm.deleteAll();
            realm.commitTransaction();
        }
        catch (Exception e) {
            realm.cancelTransaction();
        }
    }
}
