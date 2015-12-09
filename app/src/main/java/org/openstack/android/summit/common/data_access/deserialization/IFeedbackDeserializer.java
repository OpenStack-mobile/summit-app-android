package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.openstack.android.summit.common.entities.Feedback;

/**
 * Created by Claudio Redi on 11/12/2015.
 */
public interface IFeedbackDeserializer {
    Feedback deserialize(String jsonString) throws JSONException;
}
