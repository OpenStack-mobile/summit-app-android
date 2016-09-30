package org.openstack.android.summit.common.data_access.deserialization;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.entities.VenueFloor;
import org.openstack.android.summit.common.entities.VenueRoom;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.security.InvalidParameterException;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/13/2015.
 */
public class VenueRoomDeserializer extends BaseDeserializer implements IVenueRoomDeserializer {
    IDeserializerStorage deserializerStorage;
    IVenueFloorDeserializer floorDeserializer;

    @Inject
    public VenueRoomDeserializer(IDeserializerStorage deserializerStorage, IVenueFloorDeserializer floorDeserializer){
        this.deserializerStorage = deserializerStorage;
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
        VenueRoom venueRoom = deserializerStorage.exist(roomId, VenueRoom.class) ?
                deserializerStorage.get(roomId, VenueRoom.class)
                : new VenueRoom();

        venueRoom.setId(roomId);
        venueRoom.setName(jsonObject.getString("name"));
        venueRoom.setCapacity(jsonObject.has("capacity")&& !jsonObject.isNull("capacity") ? jsonObject.getInt("capacity") : 0);
        venueRoom.setLocationDescription(jsonObject.getString("description"));

        int venueId = jsonObject.getInt("venue_id");
        //first check db, and then cache storage
        Venue venue = RealmFactory.getSession().where(Venue.class).equalTo("id", venueId).findFirst();
        if(venue == null) venue = deserializerStorage.get(venueId, Venue.class);
        venueRoom.setVenue(venue);

        if(!jsonObject.isNull("floor_id")){
            int floorId      = jsonObject.getInt("floor_id");
            //first check db, and then cache storage
            VenueFloor floor = RealmFactory.getSession().where(VenueFloor.class).equalTo("id", floorId).findFirst();
            if(floor == null) floor = deserializerStorage.get(floorId, VenueFloor.class);
            venueRoom.setFloor(floor);
        }

        if(jsonObject.has("floor")){
            JSONObject jsonObjectFloor = jsonObject.getJSONObject("floor");
            //first check db, and then cache storage
            VenueFloor floor = RealmFactory.getSession().where(VenueFloor.class).equalTo("id", jsonObjectFloor.optInt("id")).findFirst();
            if(floor == null) floor = deserializerStorage.get(jsonObjectFloor.optInt("id"), VenueFloor.class);
            if(floor == null) floor = floorDeserializer.deserialize(jsonObjectFloor.toString());
            venueRoom.setFloor(floor);
        }

        if(!deserializerStorage.exist(venueRoom, VenueRoom.class)) {
            deserializerStorage.add(venueRoom, VenueRoom.class);
        }

        return venueRoom;
    }
}
