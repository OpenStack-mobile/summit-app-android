package org.openstack.android.openstacksummit.common.data_access.deserialization;

import org.json.JSONException;
import org.openstack.android.openstacksummit.common.entities.VenueRoom;

/**
 * Created by Claudio Redi on 11/16/2015.
 */
public interface IVenueRoomDeserializer {
    VenueRoom deserialize(String jsonString) throws JSONException;
}
