package org.openstack.android.summit.common.data_access.data_polling;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.data_access.IDataUpdateDataStore;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializer;
import org.openstack.android.summit.common.entities.DataUpdate;

import java.util.List;

/**
 * Created by Claudio Redi on 2/7/2016.
 */
public class DataUpdateProcessor implements IDataUpdateProcessor {
    IDeserializer deserializer;
    IDataUpdateDataStore dataUpdateDataStore;
    IDataUpdateStrategyFactory dataUpdateStrategyFactory;

    public DataUpdateProcessor(IDeserializer deserializer, IDataUpdateStrategyFactory dataUpdateStrategyFactory, IDataUpdateDataStore dataUpdateDataStore) {
        this.deserializer = deserializer;
        this.dataUpdateStrategyFactory = dataUpdateStrategyFactory;
        this.dataUpdateDataStore = dataUpdateDataStore;
    }

    @Override
    public void process(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        JSONObject jsonObject;
        DataUpdate dataUpdate;
        for(int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            dataUpdate = deserializer.deserialize(jsonObject.toString(), DataUpdate.class);
            IDataUpdateStrategy dataUpdateStrategy;
            if (dataUpdate.getEntity() != null) {
                dataUpdateStrategy = dataUpdateStrategyFactory.create(dataUpdate.getEntityClassName());
                dataUpdateStrategy.process(dataUpdate);
            }
            dataUpdateDataStore.saveOrUpdate(dataUpdate, null, DataUpdate.class);
        }
    }
}
