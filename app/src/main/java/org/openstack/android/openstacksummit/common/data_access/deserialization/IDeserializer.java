package org.openstack.android.openstacksummit.common.data_access.deserialization;

import org.json.JSONException;
import org.openstack.android.openstacksummit.common.entities.IEntity;

/**
 * Created by Claudio Redi on 11/12/2015.
 */
public interface IDeserializer {
    <T extends IEntity> T deserialize(String jsonString, Class<T> type) throws JSONException, IllegalArgumentException;
}
