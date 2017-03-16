package org.openstack.android.summit.common.data_access.data_polling;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.impl.SummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.DeleteRealmStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.SaveOrUpdateRealmStrategy;
import org.openstack.android.summit.common.entities.DataUpdate;
import org.openstack.android.summit.common.entities.IEntity;
import org.openstack.android.summit.common.entities.ISummitOwned;
import org.openstack.android.summit.common.entities.Summit;
import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 2/8/2016.
 */
public class DataUpdateStrategy implements IDataUpdateStrategy {

    protected ISummitSelector summitSelector;

    protected ISummitDataStore summitDataStore;

    protected ISaveOrUpdateStrategy saveOrUpdateStrategy;

    protected IDeleteStrategy deleteStrategy;

    public DataUpdateStrategy(ISummitSelector summitSelector) {
        this.summitSelector       = summitSelector;
        this.saveOrUpdateStrategy = new SaveOrUpdateRealmStrategy();
        this.deleteStrategy       = new DeleteRealmStrategy();
        this.summitDataStore      = new SummitDataStore(this.saveOrUpdateStrategy, this.deleteStrategy);
    }

    protected void setSummit(RealmObject entity){
        // set Summit if the entity supports it ...
        Summit summit = this.summitDataStore.getById(this.summitSelector.getCurrentSummitId());
        if(summit == null) return;
        if(entity instanceof ISummitOwned){
            ((ISummitOwned) entity).setSummit(summit);
        }
    }

    @Override
    public void process(DataUpdate dataUpdate) throws DataUpdateException {
        if (dataUpdate.getEntity() != null && dataUpdate.getEntityType() != null) {
            Intent intent = null;
            switch (dataUpdate.getOperation()) {
                case DataOperation.Insert:
                case DataOperation.Update:
                    RealmObject entity = dataUpdate.getEntity();
                    setSummit(entity);
                    this.saveOrUpdateStrategy.saveOrUpdate(entity, dataUpdate.getEntityType(), null);
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
                    deleteStrategy.delete(entityId, dataUpdate.getEntityType());
                    intent = new Intent(Constants.DATA_UPDATE_DELETED_ENTITY_EVENT);
                    intent.putExtra(Constants.DATA_UPDATE_ENTITY_ID, dataUpdate.getEntityId());
                    intent.putExtra(Constants.DATA_UPDATE_ENTITY_CLASS, dataUpdate.getEntityClassName());
                    LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).sendBroadcast(intent);
                break;
            }
        }
    }
}
