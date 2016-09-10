package org.openstack.android.summit.common.data_access.deserialization;

import android.text.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.entities.VenueFloor;

import javax.inject.Inject;

/**
 * Created by sebastian on 7/26/2016.
 */
public class VenueFloorDeserializer  extends BaseDeserializer implements IVenueFloorDeserializer {
    IDeserializerStorage deserializerStorage;

    @Inject
    public VenueFloorDeserializer(IDeserializerStorage deserializerStorage){
        this.deserializerStorage = deserializerStorage;
    }

    @Override
    public VenueFloor deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[] {"id", "name", "number", "venue_id"},  jsonObject);
        if (missedFields.length > 0) {
            throw new JSONException("Following fields are missed " + TextUtils.join(",", missedFields));
        }
        int floorId = jsonObject.getInt("id");
        VenueFloor venueFloor = deserializerStorage.exist(floorId, VenueFloor.class) ? deserializerStorage.get(floorId, VenueFloor.class) :new VenueFloor();
        venueFloor.setId(floorId);
        venueFloor.setName(jsonObject.getString("name"));
        venueFloor.setDescription(!jsonObject.isNull("description")?jsonObject.getString("description"): null);
        venueFloor.setNumber(jsonObject.getInt("number"));
        int venueId = jsonObject.getInt("venue_id");
        Venue venue = deserializerStorage.get(venueId, Venue.class);
        venueFloor.setVenue(venue);
        if(jsonObject.has("image")){
            venueFloor.setPictureUrl(!jsonObject.isNull("image")?jsonObject.getString("image"): null);
        }

        if(!deserializerStorage.exist(venueFloor, VenueFloor.class)) {
            deserializerStorage.add(venueFloor, VenueFloor.class);
        }

        return venueFloor;
    }
}
