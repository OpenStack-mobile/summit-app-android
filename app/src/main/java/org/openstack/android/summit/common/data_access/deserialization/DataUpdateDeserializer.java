package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.data_access.data_polling.DataOperation;
import org.openstack.android.summit.common.data_access.data_polling.IClassResolver;
import org.openstack.android.summit.common.entities.DataUpdate;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.IEntity;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Presentation;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;

import javax.inject.Inject;

import io.realm.RealmObject;

/**
 * Created by Claudio Redi on 11/11/2015.
 */
/*public class DataUpdateDeserializer implements IDataUpdateDeserializer {
    IClassResolver classResolver;
    IGenericDeserializer genericDeserializer;
    IPresentationSpeakerDeserializer presentationSpeakerDeserializer;
    ISummitEventDeserializer summitEventDeserializer;
    ISummitDeserializer summitDeserializer;
    IDeserializerStorage deserializerStorage;

    @Inject
    public DataUpdateDeserializer(IClassResolver classResolver,
                        IGenericDeserializer genericDeserializer,
                        IPresentationSpeakerDeserializer presentationSpeakerDeserializer,
                        ISummitEventDeserializer summitEventDeserializer,
                        ISummitDeserializer summitDeserializer,
                        IDeserializerStorage deserializerStorage)
    {
        this.classResolver = classResolver;
        this.genericDeserializer = genericDeserializer;
        this.presentationSpeakerDeserializer = presentationSpeakerDeserializer;
        this.summitEventDeserializer = summitEventDeserializer;
        this.summitDeserializer = summitDeserializer;
        this.deserializerStorage = deserializerStorage;
    }

    @Override
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
                    ? deserialize(jsonObject.get("entity").toString(), type )
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

    private <T extends RealmObject & IEntity> T deserialize(String jsonString, Class<T> type) throws JSONException {

        if (type == PresentationSpeaker.class) {
            return (T)presentationSpeakerDeserializer.deserialize(jsonString);
        }
        if (type == SummitEvent.class) {
            return (T)summitEventDeserializer.deserialize(jsonString);
        }
        if (type == Summit.class) {
            return (T)summitEventDeserializer.deserialize(jsonString);
        }
        else {
            return genericDeserializer.deserialize(jsonString, type);
        }
    }
}
*/