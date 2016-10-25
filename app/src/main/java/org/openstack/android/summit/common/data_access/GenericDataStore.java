package org.openstack.android.summit.common.data_access;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import java.util.List;

import io.realm.Realm;
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
        return RealmFactory.getSession().where(type).findAll();
    }

    @Override
    public <T extends RealmObject> List<T> getAllLocal(Class<T> type, String fieldNames[], Sort sortOrders[]) {
        RealmResults<T> result = RealmFactory.getSession().where(type).findAll();
        return result.sort(fieldNames, sortOrders);
    }

    @Override
    public <T extends RealmObject> void saveOrUpdate(final T entity, final IDataStoreOperationListener<T> delegate, Class<T> type) {
        try{
            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception {
                    T realmEntity  = session.copyToRealmOrUpdate(entity);
                    if (delegate != null) {
                        delegate.onSucceedWithSingleData(realmEntity);
                    }
                    return Void.getInstance();
                }
            });
        }
        catch (Exception ex) {
            Crashlytics.logException(ex);
            Log.e(Constants.LOG_TAG, ex.getMessage(), ex);
            if (delegate != null) {
                String friendlyError = Constants.GENERIC_ERROR_MSG;
                delegate.onError(friendlyError);
            }
        }
    }

    @Override
    public <T extends RealmObject> void delete(final int id, final IDataStoreOperationListener<T> delegate, final Class<T> type) {

        try{
            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception {
                session.where(type).equalTo("id", id).findFirst().deleteFromRealm();
                if (delegate != null) {
                    delegate.onSucceedWithoutData();
                }
                return Void.getInstance();
                }
            });
        }
        catch (Exception ex) {
            Crashlytics.logException(ex);
            Log.e(Constants.LOG_TAG, ex.getMessage(), ex);
            String friendlyError = Constants.GENERIC_ERROR_MSG;
            delegate.onError(friendlyError);
        }
    }

    @Override
    public void clearDataLocal() {
        try{
            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception {
                session.deleteAll();
                return Void.getInstance();
                }
            });
        }
        catch (Exception ex) {
            Log.e(Constants.LOG_TAG, ex.getMessage());
            Crashlytics.logException(ex);
        }
    }
}
