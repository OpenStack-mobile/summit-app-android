package org.openstack.android.summit.common.data_access.deserialization;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.entities.VenueFloor;
import org.openstack.android.summit.common.entities.VenueRoom;
import org.openstack.android.summit.common.utils.RealmFactory;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/13/2015.
 */
public class VenueRoomDeserializer extends BaseDeserializer implements IVenueRoomDeserializer {
    IVenueFloorDeserializer floorDeserializer;

    @Inject
    public VenueRoomDeserializer(IVenueFloorDeserializer floorDeserializer){
        this.floorDeserializer   = floorDeserializer;
    }

    @Override
    public VenueRoom deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[] {"id", "name", "venue_id"},  jsonObject);
        if (missedFields.length > 0) {
            throw new JSONException("Following fields are missed " + TextUtils.join(",", missedFields));
        }

        int roomId = jsonObject.getInt("id");

        VenueRoom venueRoom = RealmFactory.getSession().where(VenueRoom.class).equalTo("id", roomId).findFirst();
        if(venueRoom == null)
            venueRoom = RealmFactory.getSession().createObject(VenueRoom.class);

        venueRoom.setId(roomId);
        venueRoom.setName(jsonObject.getString("name"));
        venueRoom.setCapacity(jsonObject.has("capacity")&& !jsonObject.isNull("capacity") ? jsonObject.getInt("capacity") : 0);
        venueRoom.setLocationDescription(jsonObject.getString("description"));

        int venueId = jsonObject.getInt("venue_id");
        Venue venue = RealmFactory.getSession().where(Venue.class).equalTo("id", venueId).findFirst();
        if(venue == null)
            throw new JSONException(String.format("Can't deserialize VenueRoom id %d missing venue %d", roomId , venueId));
        venueRoom.setVenue(venue);

        if(!jsonObject.isNull("floor_id")){
            int floorId      = jsonObject.getInt("floor_id");
            VenueFloor floor = RealmFactory.getSession().where(VenueFloor.class).equalTo("id", floorId).findFirst();
            if(floor != null) venueRoom.setFloor(floor);
        }

        if(jsonObject.has("floor")){
            JSONObject jsonObjectFloor = jsonObject.getJSONObject("floor");
            VenueFloor floor = RealmFactory.getSession().where(VenueFloor.class).equalTo("id", jsonObjectFloor.optInt("id")).findFirst();
            if(floor == null) floor = floorDeserializer.deserialize(jsonObjectFloor.toString());

            if(floor != null) venueRoom.setFloor(floor);
        }
        return venueRoom;
    }
}
