package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.data_access.data_polling.DataOperation;
import org.openstack.android.summit.common.entities.DataUpdate;
import org.openstack.android.summit.common.utils.RealmFactory;

/**
 * Created by Claudio Redi on 2/7/2016.
 */
public class DataUpdateDataStore extends GenericDataStore implements IDataUpdateDataStore {
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
    public void deleteDataUpdate(DataUpdate dataUpdate) {
        RealmFactory.getSession().beginTransaction();
        try {
            dataUpdate.deleteFromRealm();
            RealmFactory.getSession().commitTransaction();
        }
        catch (Exception e) {
            RealmFactory.getSession().cancelTransaction();
            throw e;
        }
    }
}
