package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.openstack.android.summit.common.entities.SummitEventWithFile;

/**
 * Created by smarcet on 2/3/17.
 */

public interface ISummitEventWithFileDeserializer {
    SummitEventWithFile deserialize(String jsonString) throws JSONException;
}
