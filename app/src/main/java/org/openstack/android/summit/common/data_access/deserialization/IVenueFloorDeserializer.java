package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.openstack.android.summit.common.entities.VenueFloor;

/**
 * Created by sebastian on 7/26/2016.
 */
public interface IVenueFloorDeserializer {
    VenueFloor deserialize(String jsonString) throws JSONException;
}
