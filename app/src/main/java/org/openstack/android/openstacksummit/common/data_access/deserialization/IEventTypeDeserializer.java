package org.openstack.android.openstacksummit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.openstacksummit.common.entities.EventType;

/**
 * Created by Claudio Redi on 11/5/2015.
 */
public interface IEventTypeDeserializer {
    EventType deserialize(JSONObject jsonObject) throws JSONException;
}
