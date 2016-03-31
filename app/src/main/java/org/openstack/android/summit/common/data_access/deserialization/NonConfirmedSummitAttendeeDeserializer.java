package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.NonConfirmedSummitAttendee;
import org.openstack.android.summit.common.entities.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 3/30/2016.
 */
public class NonConfirmedSummitAttendeeDeserializer implements INonConfirmedSummitAttendeeDeserializer {
    @Override
    public List<NonConfirmedSummitAttendee> deserializeArray(String jsonString) throws JSONException {
        ArrayList<NonConfirmedSummitAttendee> nonConfirmedSummitAttendees = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonString);

        NonConfirmedSummitAttendee nonConfirmedSummitAttendee;
        JSONObject jsonObjectAttendee;
        JSONArray jsonArrayAttendees  = jsonObject.getJSONArray("attendees");
        for (int i = 0; i < jsonArrayAttendees.length(); i++) {
            jsonObjectAttendee = jsonArrayAttendees.getJSONObject(i);
            nonConfirmedSummitAttendee = new NonConfirmedSummitAttendee();
            nonConfirmedSummitAttendee.setId(jsonObjectAttendee.optInt("external_id"));
            nonConfirmedSummitAttendee.setName(
                    jsonObjectAttendee.optString("first_name") + " " + jsonObjectAttendee.optString("last_name")
            );
            nonConfirmedSummitAttendees.add(nonConfirmedSummitAttendee);
        }

        return nonConfirmedSummitAttendees;
    }
}
