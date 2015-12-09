package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.openstack.android.summit.common.entities.SummitEvent;

/**
 * Created by Claudio Redi on 11/16/2015.
 */
public interface ISummitEventDeserializer {
    SummitEvent deserialize(String jsonString) throws JSONException;
}
