package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.data_access.data_polling.DataOperation;
import org.openstack.android.summit.common.entities.DataUpdate;

/**
 * Created by Claudio Redi on 2/7/2016.
 */
public class DataUpdateDataStore extends GenericDataStore implements IDataUpdateDataStore {
    @Override
    public int getLatestDataUpdate() {
        //HACK: this is a for multithreading. To avoid error: " Realm access from incorrect thread. Realm objects can only be accessed on the thread they were created."
        // Check if this is causing considerable memory usage increase
        Number latestId = realm.where(DataUpdate.class).max("id");
        return latestId != null ? latestId.intValue() : 0;
    }

    @Override
    public DataUpdate getTruncateDataUpdate() {
        DataUpdate dataUpdate = realm.where(DataUpdate.class).equalTo("operation", DataOperation.Truncate).findFirst();
        return dataUpdate;
    }

    @Override
    public void deleteDataUpdate(DataUpdate dataUpdate) {
        realm.beginTransaction();
        try {
            dataUpdate.deleteFromRealm();
            realm.commitTransaction();
        }
        catch (Exception e) {
            realm.cancelTransaction();
            throw e;
        }
    }
}
