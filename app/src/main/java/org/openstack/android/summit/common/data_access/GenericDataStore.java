package org.openstack.android.summit.common.data_access;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public class GenericDataStore implements IGenericDataStore {
    Realm realm = Realm.getDefaultInstance();

    @Override
    public <T extends RealmObject> T getByIdLocal(int id, Class<T> type) {
        return realm.where(type).equalTo("id", id).findFirst();
    }

    @Override
    public <T extends RealmObject> List<T> getaAllLocal(Class<T> type) {
        //HACK: this is a for multithreading. To avoid error: " Realm access from incorrect thread. Realm objects can only be accessed on the thread they were created."
        // Check if this is causing considerable memory usage increase
        Realm realm = Realm.getDefaultInstance();
        ArrayList<T> list = new ArrayList<>();
        RealmResults<T> result = realm.where(type).findAll();
        list.addAll(result.subList(0, result.size()));
        return list;
    }

    @Override
    public <T extends RealmObject> void saveOrUpdate(final T entity, IDataStoreOperationListener<T> delegate, Class<T> type) {
        T realmEntity;
        try{
            realm.beginTransaction();
            realmEntity = realm.copyToRealmOrUpdate(entity);
            realm.commitTransaction();
            if (delegate != null) {
                delegate.onSuceedWithSingleData(realmEntity);
            }
        }
        catch (Exception ex) {
            delegate.onError(ex.getMessage());
        }
    }

    @Override
    public <T extends RealmObject> void delete(int id, IDataStoreOperationListener<T> delegate, Class<T> type) {
        try{
            realm.beginTransaction();
            realm.where(type).equalTo("id", id).findFirst().removeFromRealm();
            realm.commitTransaction();
            if (delegate != null) {
                delegate.onSucceedWithoutData();
            }
        }
        catch (Exception ex) {
            delegate.onError(ex.getMessage());
        }
    }
}
