package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitGroupEvent;
import org.openstack.android.summit.common.utils.RealmFactory;

/**
 * Created by smarcet on 1/30/17.
 */

public class SummitGroupEventDeserializer extends BaseDeserializer implements IGroupEventDeserializer {

    @Override
    public SummitGroupEvent deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[] {"id"},  jsonObject);
        handleMissedFieldsIfAny(missedFields);
        int groupEventId = jsonObject.getInt("id");

        SummitGroupEvent groupEvent = RealmFactory.getSession().where(SummitGroupEvent.class).equalTo("id", groupEventId).findFirst();
        if(groupEvent == null)
            groupEvent = RealmFactory.getSession().createObject(SummitGroupEvent.class, groupEventId);

        return groupEvent;
    }
}
