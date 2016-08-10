package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.openstack.android.summit.common.entities.PresentationLink;

/**
 * Created by sebastian on 8/10/2016.
 */
public interface IPresentationLinkDeserializer {
    PresentationLink deserialize(String jsonString) throws JSONException;
}
