package org.openstack.android.summit.common.data_access.deserialization;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.entities.VenueFloor;
import org.openstack.android.summit.common.entities.VenueRoom;

import java.security.InvalidParameterException;

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
        venueRoom.setCapacity(jsonObject.has("capacity")&& !jsonObject.isNull("capacity") ? jsonObject.getInt("capacity") : 0);
        venueRoom.setLocationDescription(jsonObject.getString("description"));

        int venueId = jsonObject.getInt("venue_id");
        Venue venue = deserializerStorage.get(venueId, Venue.class);
        venueRoom.setVenue(venue);

        if(!jsonObject.isNull("floor_id")){
            int floorId = jsonObject.getInt("floor_id");
            VenueFloor floor = deserializerStorage.get(floorId, VenueFloor.class);
            venueRoom.setFloor(floor);
        }
        if(!jsonObject.isNull("floor")){
            JSONObject jsonObjectFloor = jsonObject.getJSONObject("floor");
            VenueFloor floor           = deserializerStorage.get(jsonObjectFloor.getInt("id"), VenueFloor.class);
            if(floor == null)
                throw new InvalidParameterException(String.format("floor id %s not found on database!", jsonObjectFloor.getInt("id")));
            venueRoom.setFloor(floor);
        }
        if(!deserializerStorage.exist(venueRoom, VenueRoom.class)) {
            deserializerStorage.add(venueRoom, VenueRoom.class);
        }

        return venueRoom;
    }
}
