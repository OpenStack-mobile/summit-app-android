package org.openstack.android.summit.common.data_access.data_polling;

import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.entities.DataUpdate;
import org.openstack.android.summit.common.entities.IEntity;

/**
 * Created by Claudio Redi on 2/8/2016.
 */
public class DataUpdateStrategy implements IDataUpdateStrategy {
    protected IGenericDataStore genericDataStore;


    public DataUpdateStrategy(IGenericDataStore genericDataStore) {
        this.genericDataStore = genericDataStore;
    }

    @Override
    public void process(DataUpdate dataUpdate) throws DataUpdateException {
        if (dataUpdate.getEntity() != null && dataUpdate.getEntityType() != null) {
            switch (dataUpdate.getOperation()) {
                case DataOperation.Insert:
                case DataOperation.Update:
                    genericDataStore.saveOrUpdate(dataUpdate.getEntity(), null, dataUpdate.getEntityType());
                    break;
                case DataOperation.Delete:
                    int entityId = ((IEntity) dataUpdate.getEntity()).getId();
                    genericDataStore.delete(entityId, null, dataUpdate.getEntityType());
            }
        }
    }
}
