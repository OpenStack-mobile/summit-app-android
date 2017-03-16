package org.openstack.android.summit.common.data_access.data_polling;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.IWifiConnectionDataStore;
import org.openstack.android.summit.common.entities.DataUpdate;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitWIFIConnection;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

/**
 * Created by sebastian on 8/10/2016.
 */
public class WifiConnectionDataUpdateStrategy extends DataUpdateStrategy  {

    private IWifiConnectionDataStore wifiConnectionDataStore;

    public WifiConnectionDataUpdateStrategy
    (
        IWifiConnectionDataStore wifiConnectionDataStore,
        ISummitSelector summitSelector
    ) {
        super(summitSelector);

        this.wifiConnectionDataStore = wifiConnectionDataStore;
    }

    @Override
    public void process(final DataUpdate dataUpdate) throws DataUpdateException {
        final String className      = dataUpdate.getEntityClassName();
        switch (dataUpdate.getOperation()) {
            case DataOperation.Insert:
                final JSONObject entityJSON = dataUpdate.getOriginalJSON().optJSONObject("entity");
                if(entityJSON == null) return;
                final Integer summit_id = entityJSON.optInt("summit_id");

                if (summit_id == null)
                    throw new DataUpdateException("It wasn't possible to find summit_id on data update json");

                try {

                    RealmFactory.transaction(session -> {
                        Summit managedSummit = summitDataStore.getById(summit_id);
                        if (managedSummit == null)
                            throw new DataUpdateException(String.format("Summit with id %d not found", summit_id));

                        if (className.equals("SummitWIFIConnection")) {
                            SummitWIFIConnection wifiConnection = (SummitWIFIConnection) dataUpdate.getEntity();
                            wifiConnection.setSummit(managedSummit);
                            managedSummit.getWifiConnections().add(wifiConnection);
                        }
                        return Void.getInstance();
                    });
                }
                catch (Exception ex){
                    Crashlytics.logException(ex);
                    Log.e(Constants.LOG_TAG, ex.getMessage(), ex);
                }
                break;
            case DataOperation.Update:
                try {
                    final JSONObject entityJSONUpdate = dataUpdate.getOriginalJSON().optJSONObject("entity");
                    if (entityJSONUpdate == null) return;

                    if (className.equals("SummitWIFIConnection")) {
                        wifiConnectionDataStore.saveOrUpdate((SummitWIFIConnection)dataUpdate.getEntity());
                    }
                }
                catch (Exception ex){
                    Crashlytics.logException(ex);
                    Log.e(Constants.LOG_TAG, ex.getMessage(), ex);
                }
                break;
            case DataOperation.Delete:
                if(className.equals("SummitWIFIConnection")){
                    wifiConnectionDataStore.delete(((SummitWIFIConnection)dataUpdate.getEntity()).getId());
                }
                break;
        }
    }
}

