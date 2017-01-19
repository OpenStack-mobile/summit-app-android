package org.openstack.android.summit.common.data_access.deserialization;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.Image;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.entities.VenueFloor;
import org.openstack.android.summit.common.utils.RealmFactory;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/13/2015.
 */
public class VenueDeserializer extends BaseDeserializer implements IVenueDeserializer {
    IGenericDeserializer genericDeserializer;
    IVenueFloorDeserializer venueFloorDeserializer;

    @Inject
    public VenueDeserializer(IGenericDeserializer genericDeserializer, IVenueFloorDeserializer venueFloorDeserializer){
        this.genericDeserializer    = genericDeserializer;
        this.venueFloorDeserializer = venueFloorDeserializer;
    }

    @Override
    public Venue deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[] {"id", "name", "location_type"},  jsonObject);
        if (missedFields.length > 0) {
            throw new JSONException("Following fields are missed " + TextUtils.join(",", missedFields));
        }
        int venueId = jsonObject.getInt("id");

        Venue venue = RealmFactory.getSession().where(Venue.class).equalTo("id", venueId).findFirst();
        if(venue == null)
            venue = RealmFactory.getSession().createObject(Venue.class, venueId);

        venue.setName(jsonObject.getString("name"));
        venue.setLocationDescription(
                !jsonObject.isNull("description") ? jsonObject.getString("description") : null
        );
        venue.setLat(
                !jsonObject.isNull("lat") ? jsonObject.getString("lat") : null
        );
        venue.setLng(
                !jsonObject.isNull("lng") ? jsonObject.getString("lng") : null
        );
        venue.setAddress(
                !jsonObject.isNull("address_1") ? jsonObject.getString("address_1") : null
        );
        venue.setCity(
                !jsonObject.isNull("city") ? jsonObject.getString("city") : null
        );
        venue.setState(
                !jsonObject.isNull("state") ? jsonObject.getString("state") : null
        );
        venue.setZipCode(
                !jsonObject.isNull("zip_code") ? jsonObject.getString("zip_code") : null
        );
        venue.setCountry(
                !jsonObject.isNull("country") ? jsonObject.getString("country") : null
        );
        venue.setIsInternal(jsonObject.getString("class_name").equals("SummitVenue"));

        Image map;
        JSONObject jsonObjectMap;
        JSONArray jsonArrayMap = jsonObject.getJSONArray("maps");
        venue.getMaps().clear();
        for (int i = 0; i < jsonArrayMap.length(); i++) {
            jsonObjectMap = jsonArrayMap.getJSONObject(i);
            map = genericDeserializer.deserialize(jsonObjectMap.toString(), Image.class);
            venue.getMaps().add(map);
        }

        Image image;
        JSONObject jsonObjectImage;
        JSONArray jsonArrayImages = jsonObject.getJSONArray("images");
        venue.getImages().clear();
        for (int i = 0; i < jsonArrayImages.length(); i++) {
            jsonObjectImage = jsonArrayImages.getJSONObject(i);
            image = genericDeserializer.deserialize(jsonObjectImage.toString(), Image.class);
            venue.getImages().add(image);
        }

        if(jsonObject.has("floors")) {
            VenueFloor floor;
            JSONObject jsonObjectFloor;
            JSONArray jsonArrayFloors = jsonObject.getJSONArray("floors");
            venue.getFloors().clear();
            for (int i = 0; i < jsonArrayFloors.length(); i++) {
                jsonObjectFloor = jsonArrayFloors.getJSONObject(i);
                floor           = venueFloorDeserializer.deserialize(jsonObjectFloor.toString());
                venue.getFloors().add(floor);
                floor.setVenue(venue);
            }
        }

        return venue;
    }
}
