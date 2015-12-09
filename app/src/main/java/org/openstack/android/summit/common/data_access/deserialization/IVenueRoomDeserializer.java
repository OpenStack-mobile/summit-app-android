package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.openstack.android.summit.common.entities.VenueRoom;

/**
 * Created by Claudio Redi on 11/16/2015.
 */
public interface IVenueRoomDeserializer {
    VenueRoom deserialize(String jsonString) throws JSONException;
}
