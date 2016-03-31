package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.openstack.android.summit.common.entities.NonConfirmedSummitAttendee;

import java.util.List;

/**
 * Created by Claudio Redi on 3/30/2016.
 */
public interface INonConfirmedSummitAttendeeDeserializer {
    List<NonConfirmedSummitAttendee> deserializeArray(String jsonString) throws JSONException;
}
