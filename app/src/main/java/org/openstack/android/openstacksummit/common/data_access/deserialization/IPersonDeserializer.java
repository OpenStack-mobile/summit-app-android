package org.openstack.android.openstacksummit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.openstacksummit.common.entities.IPerson;

/**
 * Created by Claudio Redi on 11/16/2015.
 */
public interface IPersonDeserializer {
    void deserialize(IPerson person, JSONObject jsonObject) throws JSONException;
}
