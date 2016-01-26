package org.openstack.android.summit.common.data_access.deserialization;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.entities.VenueRoom;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/13/2015.
 */
public class VenueRoomDeserializer extends BaseDeserializer implements IVenueRoomDeserializer {
    IDeserializerStorage deserializerStorage;

    @Inject
    public VenueRoomDeserializer(IDeserializerStorage deserializerStorage){
        this.deserializerStorage = deserializerStorage;
    }

    @Override
    public VenueRoom deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[] {"id", "name", "venue_id"},  jsonObject);
        if (missedFields.length > 0) {
            throw new JSONException("Following fields are missed " + TextUtils.join(",", missedFields));
        }

        VenueRoom venueRoom = new VenueRoom();
        venueRoom.setId(jsonObject.getInt("id"));
        venueRoom.setName(jsonObject.getString("name"));
        venueRoom.setCapacity(jsonObject.has("Capaciry") ? jsonObject.getInt("Capacity") : 0);
        venueRoom.setLocationDescription(jsonObject.getString("description"));
        int venueId = jsonObject.getInt("venue_id");
        Venue venue = deserializerStorage.get(venueId, Venue.class);
        venueRoom.setVenue(venue);

        if(!deserializerStorage.exist(venueRoom, VenueRoom.class)) {
            deserializerStorage.add(venueRoom, VenueRoom.class);
        }

        return venueRoom;
    }
}
