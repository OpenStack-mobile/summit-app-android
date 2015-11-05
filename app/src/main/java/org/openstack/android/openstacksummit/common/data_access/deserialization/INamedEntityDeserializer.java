package org.openstack.android.openstacksummit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.openstacksummit.common.entities.INamedEntity;

/**
 * Created by Claudio Redi on 11/5/2015.
 */
public interface INamedEntityDeserializer {
    void deserialize(JSONObject jsonObject, INamedEntity entity) throws JSONException;
}
