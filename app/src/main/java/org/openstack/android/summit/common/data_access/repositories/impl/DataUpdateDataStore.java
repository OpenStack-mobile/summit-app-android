package org.openstack.android.summit.common.data_access.repositories.impl;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.repositories.IDataUpdateDataStore;
import org.openstack.android.summit.common.data_access.data_polling.DataOperation;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.DataUpdate;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import io.realm.Realm;

/**
 * Created by Claudio Redi on 2/7/2016.
 */
public class DataUpdateDataStore extends GenericDataStore<DataUpdate> implements IDataUpdateDataStore {

    public DataUpdateDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy, IDeleteStrategy deleteStrategy) {
        super(DataUpdate.class, saveOrUpdateStrategy, deleteStrategy);
    }

    @Override
    public int getLatestDataUpdate() {
        //HACK: this is a for multithreading. To avoid error: " Realm access from incorrect thread. Realm objects can only be accessed on the thread they were created."
        // Check if this is causing considerable memory usage increase
        Number latestId = RealmFactory.getSession().where(DataUpdate.class).max("id");
        return latestId != null ? latestId.intValue() : 0;
    }

    @Override
    public DataUpdate getTruncateDataUpdate() {
        return RealmFactory.getSession().where(DataUpdate.class).equalTo("operation", DataOperation.Truncate).findFirst();
    }

    @Override
    public void deleteDataUpdate(final DataUpdate dataUpdate) {
        try{
            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception {
                    dataUpdate.deleteFromRealm();
                    return Void.getInstance();
                }
            });
        }
        catch (Exception ex) {
            Crashlytics.logException(ex);
            Log.e(Constants.LOG_TAG, ex.getMessage(), ex);
        }
    }

}
