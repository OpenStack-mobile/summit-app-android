package org.openstack.android.summit.common.data_access.deserialization;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.Image;
import org.openstack.android.summit.common.entities.Venue;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/13/2015.
 */
public class VenueDeserializer extends BaseDeserializer implements IVenueDeserializer {
    IGenericDeserializer genericDeserializer;
    IDeserializerStorage deserializerStorage;

    @Inject
    public VenueDeserializer(IGenericDeserializer genericDeserializer, IDeserializerStorage deserializerStorage){
        this.genericDeserializer = genericDeserializer;
        this.deserializerStorage = deserializerStorage;
    }

    @Override
    public Venue deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[] {"id", "lat", "lng", "address_1", "location_type"},  jsonObject);
        if (missedFields.length > 0) {
            throw new JSONException("Following fields are missed " + TextUtils.join(",", missedFields));
        }
        Venue venue = new Venue();
        venue.setId(jsonObject.getInt("id"));
        venue.setName(jsonObject.getString("name"));
        venue.setLocationDescription(jsonObject.getString("description"));
        venue.setLat(jsonObject.getString("lat"));
        venue.setLng(jsonObject.getString("lng"));
        venue.setAddress(jsonObject.getString("address_1"));
        venue.setCity(!jsonObject.isNull("city") ? jsonObject.getString("city") : null);
        venue.setState(!jsonObject.isNull("state") ? jsonObject.getString("state") : null);
        venue.setZipCode(!jsonObject.isNull("zip_code") ? jsonObject.getString("zip_code") : null);
        venue.setCountry(!jsonObject.isNull("country") ? jsonObject.getString("country") : null);
        venue.setIsInternal(jsonObject.getString("class_name").equals("SummitVenue"));

        Image map;
        JSONObject jsonObjectMap;
        JSONArray jsonArrayMap = jsonObject.getJSONArray("maps");
        for (int i = 0; i < jsonArrayMap.length(); i++) {
            jsonObjectMap = jsonArrayMap.getJSONObject(i);
            map = genericDeserializer.deserialize(jsonObjectMap.toString(), Image.class);
            venue.getMaps().add(map);
        }

        Image image;
        JSONObject jsonObjectImage;
        JSONArray jsonArrayImages = jsonObject.getJSONArray("images");
        for (int i = 0; i < jsonArrayImages.length(); i++) {
            jsonObjectImage = jsonArrayImages.getJSONObject(i);
            image = genericDeserializer.deserialize(jsonObjectImage.toString(), Image.class);
            venue.getImages().add(image);
        }

        if(!deserializerStorage.exist(venue, Venue.class)) {
            deserializerStorage.add(venue, Venue.class);
        }

        return venue;
    }
}
