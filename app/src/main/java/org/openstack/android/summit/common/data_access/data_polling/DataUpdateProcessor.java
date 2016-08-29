package org.openstack.android.summit.common.data_access.data_polling;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.Constants;
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
        this.deserializer              = deserializer;
        this.dataUpdateStrategyFactory = dataUpdateStrategyFactory;
        this.dataUpdateDataStore       = dataUpdateDataStore;
        this.classResolver             = classResolver;
        this.deserializerStorage       = deserializerStorage;
    }

    @Override
    public void process(String json) throws JSONException {

        JSONArray jsonArray   = new JSONArray(json);
        JSONObject jsonObject = null;
        DataUpdate dataUpdate = null;

        Log.d(Constants.LOG_TAG, String.format("Thread %s Data updates : %s", Thread.currentThread().getName(), jsonArray.toString(4)));

        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                jsonObject = jsonArray.getJSONObject(i);
                dataUpdate = deserialize(jsonObject.toString());

                IDataUpdateStrategy dataUpdateStrategy;
                if (dataUpdate.getEntity() != null) {
                    dataUpdateStrategy = dataUpdateStrategyFactory.create(dataUpdate.getEntityClassName());
                    dataUpdateStrategy.process(dataUpdate);
                }
            }
            catch (Exception e) {
                String errorMessage = jsonObject != null ? String.format("There was an error processing this data update: %s", jsonObject.toString()) : "";
                Crashlytics.logException(new Exception(errorMessage, e));
                Log.e(Constants.LOG_TAG, errorMessage, e);
            }
            finally {
                if (dataUpdate != null) {
                    dataUpdateDataStore.saveOrUpdate(dataUpdate, null, DataUpdate.class);
                }
            }
        }
    }

    public DataUpdate deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        DataUpdate dataUpdate = new DataUpdate();

        dataUpdate.setId(jsonObject.getInt("id"));
        dataUpdate.setOriginalJSON(jsonObject);
        String operationType = jsonObject.getString("type");
        
        if (!operationType.equals("TRUNCATE")) {
            String className = jsonObject.getString("class_name");
            if (!isClassNameKnownIgnored(className)) {
                Class type = null;
                try {
                    type = classResolver.fromName(className);
                } catch (ClassNotFoundException e) {
                    throw new JSONException(String.format("It wasn't possible to deserialize json for className %s", className, e));
                }
                RealmObject entity = !operationType.equals("DELETE") && !className.equals("MySchedule")
                        ? deserializer.deserialize(jsonObject.get("entity").toString(), type )
                        : deserializerStorage.get(jsonObject.getInt("entity_id"), type);

                dataUpdate.setEntityType(type);
                dataUpdate.setEntity(entity);
            }
            dataUpdate.setEntityClassName(className);
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

    private boolean isClassNameKnownIgnored(String className) {
        return className == "PresentationVideo" || className == "SummitHotel" ;
    }
}
