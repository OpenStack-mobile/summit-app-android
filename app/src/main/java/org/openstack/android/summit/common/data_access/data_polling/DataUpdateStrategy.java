package org.openstack.android.summit.common.data_access.data_polling;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.Constants;
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
            Intent intent = null;
            switch (dataUpdate.getOperation()) {
                case DataOperation.Insert:
                case DataOperation.Update:
                    genericDataStore.saveOrUpdate(dataUpdate.getEntity(), null, dataUpdate.getEntityType());
                    String event  = dataUpdate.getOperation() == DataOperation.Insert ?
                            Constants.DATA_UPDATE_ADDED_ENTITY_EVENT :
                            Constants.DATA_UPDATE_UPDATED_ENTITY_EVENT ;

                    intent = new Intent(event);
                    intent.putExtra(Constants.DATA_UPDATE_ENTITY_ID, dataUpdate.getEntityId());
                    intent.putExtra(Constants.DATA_UPDATE_ENTITY_CLASS, dataUpdate.getEntityClassName());
                    LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).sendBroadcast(intent);
                break;
                case DataOperation.Delete:
                    int entityId = ((IEntity) dataUpdate.getEntity()).getId();
                    genericDataStore.delete(entityId, null, dataUpdate.getEntityType());
                    intent = new Intent( Constants.DATA_UPDATE_DELETED_ENTITY_EVENT);
                    intent.putExtra(Constants.DATA_UPDATE_ENTITY_ID, dataUpdate.getEntityId());
                    intent.putExtra(Constants.DATA_UPDATE_ENTITY_CLASS, dataUpdate.getEntityClassName());
                    LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).sendBroadcast(intent);
                break;
            }
        }
    }
}
