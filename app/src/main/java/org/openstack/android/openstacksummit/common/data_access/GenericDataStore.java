package org.openstack.android.openstacksummit.common.data_access;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public class GenericDataStore {
    Realm realm = Realm.getDefaultInstance();

    public <T extends RealmObject> T getByIdLocal(int id, Class<T> type) {
        return realm.where(type).equalTo("id", id).findFirst();
    }

    public <T extends RealmObject> List<T> getaAllLocal(Class<T> type) {
        ArrayList<T> list = new ArrayList<>();
        RealmResults<T> result = realm.where(type).findAll();
        list.addAll(result.subList(0, result.size()));
        return list;
    }

    public <T extends RealmObject> void SaveOrUpdate(final T entity, IDataStoreOperationListener<T> delegate, Class<T> type) {
        T realmEntity;
        try{
            realm.beginTransaction();
            realmEntity = realm.copyToRealmOrUpdate(entity);
            realm.commitTransaction();
            if (delegate != null) {
                delegate.onSuceedWithData(realmEntity);
            }
        }
        catch (Exception ex) {
            delegate.onError(ex.getMessage());
        }
    }

    public <T extends RealmObject> void delete(int id, IDataStoreOperationListener<T> delegate, Class<T> type) {
        try{
            realm.beginTransaction();
            realm.where(type).equalTo("id", id).findFirst().removeFromRealm();
            realm.commitTransaction();
            if (delegate != null) {
                delegate.onSucceed();
            }
        }
        catch (Exception ex) {
            delegate.onError(ex.getMessage());
        }
    }
}
