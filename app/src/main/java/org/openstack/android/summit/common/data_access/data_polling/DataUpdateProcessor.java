package org.openstack.android.summit.common.data_access.data_polling;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.data_access.IDataUpdateDataStore;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IDeserializerStorage;
import org.openstack.android.summit.common.entities.DataUpdate;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 2/7/2016.
 */
public class DataUpdateProcessor implements IDataUpdateProcessor {
    IDeserializer deserializer;
    IDataUpdateDataStore dataUpdateDataStore;
    IDataUpdateStrategyFactory dataUpdateStrategyFactory;
    IClassResolver classResolver;
    IDeserializerStorage deserializerStorage;

    public DataUpdateProcessor(IDeserializer deserializer, IDataUpdateStrategyFactory dataUpdateStrategyFactory, IDataUpdateDataStore dataUpdateDataStore, IClassResolver classResolver, IDeserializerStorage deserializerStorage) {
        this.deserializer = deserializer;
        this.dataUpdateStrategyFactory = dataUpdateStrategyFactory;
        this.dataUpdateDataStore = dataUpdateDataStore;
        this.classResolver = classResolver;
        this.dataUpdateDataStore = dataUpdateDataStore;
    }

    @Override
    public void process(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        JSONObject jsonObject;
        DataUpdate dataUpdate;
        for(int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            dataUpdate = deserialize(jsonObject.toString());
            IDataUpdateStrategy dataUpdateStrategy;
            if (dataUpdate.getEntity() != null) {
                dataUpdateStrategy = dataUpdateStrategyFactory.create(dataUpdate.getEntityClassName());
                dataUpdateStrategy.process(dataUpdate);
            }
            dataUpdateDataStore.saveOrUpdate(dataUpdate, null, DataUpdate.class);
        }
    }

    public DataUpdate deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        DataUpdate dataUpdate = new DataUpdate();
        dataUpdate.setId(jsonObject.getInt("id"));
        String operationType = jsonObject.getString("type");

        if (!operationType.equals("TRUNCATE")) {
            String className = jsonObject.getString("class_name");
            Class type = null;
            try {
                type = classResolver.fromName(className);
            } catch (ClassNotFoundException e) {
                throw new JSONException(String.format("It wasn't possible to desirialize json for className %s"));
            }
            RealmObject entity = !operationType.equals("DELETE") && !className.equals("MySchedule")
                    ? deserializer.deserialize(jsonObject.get("entity").toString(), type )
                    : deserializerStorage.get(jsonObject.getInt("entity_id"), type);

            dataUpdate.setEntityType(type);
            dataUpdate.setEntityClassName(className);
            dataUpdate.setEntity(entity);
        }

        switch (operationType) {
            case "INSERT":
                dataUpdate.setOperation(DataOperation.Insert);
                break;
            case "UPDATE":
                dataUpdate.setOperation(DataOperation.Update);
                break;
            case "DELETE":
                dataUpdate.setOperation(DataOperation.Delete);
                break;
            case "TRUNCATE":
                dataUpdate.setOperation(DataOperation.Truncate);
                break;
        }
        return dataUpdate;
    }
}
