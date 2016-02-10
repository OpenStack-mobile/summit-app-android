package org.openstack.android.summit.common.data_access.data_polling;

import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.entities.DataUpdate;

/**
 * Created by Claudio Redi on 2/8/2016.
 */
public class DataUpdateStrategy implements IDataUpdateStrategy {
    private IGenericDataStore genericDataStore;


    public DataUpdateStrategy(IGenericDataStore genericDataStore) {
        this.genericDataStore = genericDataStore;
    }

    @Override
    public void process(DataUpdate dataUpdate) {
        switch (dataUpdate.getOperation()) {
            case DataOperation.Insert:
            case DataOperation.Update:
                genericDataStore.saveOrUpdate(dataUpdate.getEntity(), null, dataUpdate.getEntityType());
                break;
            case DataOperation.Delete:
                if (dataUpdate.getEntity() != null) {
                    //genericDataStore.delete(dataUpdate.getEntity().get, null);
                }
        }
    }
}
