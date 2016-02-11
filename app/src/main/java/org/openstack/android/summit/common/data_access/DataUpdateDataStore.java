package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.DataUpdate;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Claudio Redi on 2/7/2016.
 */
public class DataUpdateDataStore extends GenericDataStore implements IDataUpdateDataStore {
    @Override
    public int getLatestDataUpdate() {
        Realm realm = Realm.getDefaultInstance();
        Number latestId = realm.where(DataUpdate.class).max("id");
        return latestId != null ? latestId.intValue() : 0;
    }
}
