package org.openstack.android.openstacksummit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.openstacksummit.common.entities.Company;
import org.openstack.android.openstacksummit.common.entities.EventType;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/5/2015.
 */
public class EventTypeDeserializer implements IEventTypeDeserializer {
    @Inject
    INamedEntityDeserializer namedEntityDeserializer;

    @Inject
    public EventTypeDeserializer() {}

    @Override
    public EventType deserialize(JSONObject jsonObject) throws JSONException {
        EventType entity = new EventType();
        namedEntityDeserializer.deserialize(jsonObject, entity);
        return entity;
    }
}
