package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.openstack.android.summit.common.entities.SummitWIFIConnection;

/**
 * Created by Claudio Redi on 2/9/2016.
 */
public interface IWifiConnectionDeserializer {
    SummitWIFIConnection deserialize(String jsonString) throws JSONException;

}
