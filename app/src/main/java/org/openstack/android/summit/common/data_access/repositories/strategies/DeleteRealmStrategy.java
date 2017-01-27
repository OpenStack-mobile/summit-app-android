package org.openstack.android.summit.common.data_access.repositories.strategies;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.IDataStoreOperationListener;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by smarcet on 1/25/17.
 */

public class DeleteRealmStrategy implements IDeleteStrategy {
    @Override
    public <T extends RealmObject> void delete(final int id, final Class<T> type, final IDataStoreOperationListener<T> callback) {
        try{
            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception {
                    T object = session.where(type).equalTo("id", id).findFirst()   ;
                    if(object != null) object.deleteFromRealm();
                    if (callback != null) {
                        callback.onSucceedWithoutData();
                    }
                    return Void.getInstance();
                }
            });
        }
        catch (Exception ex) {
            Crashlytics.logException(ex);
            Log.e(Constants.LOG_TAG, ex.getMessage(), ex);
            String friendlyError = Constants.GENERIC_ERROR_MSG;
            callback.onError(friendlyError);
        }
    }

    @Override
    public <T extends RealmObject> void delete(int id, Class<T> type) {
        this.delete(id, type, null);
    }
}
