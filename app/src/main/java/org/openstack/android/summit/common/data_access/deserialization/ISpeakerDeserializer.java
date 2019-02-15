package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.openstack.android.summit.common.entities.Speaker;

/**
 * Created by Claudio Redi on 11/16/2015.
 */
public interface ISpeakerDeserializer {
    Speaker deserialize(String jsonString) throws JSONException;
}
