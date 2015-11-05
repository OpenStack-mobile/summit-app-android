package org.openstack.android.openstacksummit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.openstacksummit.common.entities.INamedEntity;

/**
 * Created by Claudio Redi on 11/5/2015.
 */
public class NamedEntityDeserializer implements INamedEntityDeserializer {
    @Override
    public void deserialize(JSONObject jsonObject, INamedEntity entity) throws JSONException {
        entity.setId(jsonObject.getInt("id"));
        entity.setName(jsonObject.getString("name"));
    }
}
