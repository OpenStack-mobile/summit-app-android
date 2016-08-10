package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.openstack.android.summit.common.entities.PresentationSlide;

/**
 * Created by sebastian on 8/10/2016.
 */
public interface IPresentationSlideDeserializer {
    PresentationSlide deserialize(String jsonString) throws JSONException;
}
