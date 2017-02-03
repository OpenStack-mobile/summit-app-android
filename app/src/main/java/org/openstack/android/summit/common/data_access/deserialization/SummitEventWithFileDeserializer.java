package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.SummitEventWithFile;
import org.openstack.android.summit.common.utils.RealmFactory;

/**
 * Created by smarcet on 2/3/17.
 */

public class SummitEventWithFileDeserializer extends BaseDeserializer implements ISummitEventWithFileDeserializer {

    @Override
    public SummitEventWithFile deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[] {"id"},  jsonObject);
        handleMissedFieldsIfAny(missedFields);
        int eventId = jsonObject.getInt("id");

        SummitEventWithFile event = RealmFactory.getSession().where(SummitEventWithFile.class).equalTo("id", eventId).findFirst();
        if(event == null)
            event = RealmFactory.getSession().createObject(SummitEventWithFile.class, eventId);

        if(jsonObject.has("attachment")){
            event.setAttachment(jsonObject.getString("attachment"));
        }

        return event;
    }
}
