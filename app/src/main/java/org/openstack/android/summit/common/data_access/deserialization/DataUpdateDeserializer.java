package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.DataUpdate;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/11/2015.
 */
public class DataUpdateDeserializer {
    @Inject
    public DataUpdateDeserializer() {

    }

    public DataUpdate deserialize(JSONObject jsonObject) throws JSONException {
        DataUpdate entity = new DataUpdate();
        String className = jsonObject.getString("name");

        return entity;
    }
}

/*    let dataUpdate = DataUpdate()
    let className = json["class_name"].stringValue
    let deserializer = try deserializerFactory.create(className)
    let operationType = json["type"]

    dataUpdate.id = json["id"].intValue
    dataUpdate.entityClassName = className
    dataUpdate.entity = try deserializer.deserialize(json["entity"])

            switch (operationType) {
        case "INSERT":
            dataUpdate.operation = DataOperation.Insert
        case "UPDATE":
            dataUpdate.operation = DataOperation.Update
        case "DELETE":
            dataUpdate.operation = DataOperation.Delete
        default:
            throw DeserializerError.BadFormat("Operation is not valid")
    }

    return dataUpdate
}*/
