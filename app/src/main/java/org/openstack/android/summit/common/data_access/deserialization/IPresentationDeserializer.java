package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.openstack.android.summit.common.entities.Presentation;

/**
 * Created by Claudio Redi on 11/16/2015.
 */
public interface IPresentationDeserializer {
    Presentation deserialize(String jsonString) throws JSONException;
}
