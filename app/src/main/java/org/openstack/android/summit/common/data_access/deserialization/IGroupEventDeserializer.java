package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.openstack.android.summit.common.entities.SummitGroupEvent;

/**
 * Created by smarcet on 1/30/17.
 */

public interface IGroupEventDeserializer {

    SummitGroupEvent deserialize(String jsonString) throws JSONException;
}
