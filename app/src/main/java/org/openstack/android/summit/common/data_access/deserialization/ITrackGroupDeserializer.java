package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.openstack.android.summit.common.entities.TrackGroup;

/**
 * Created by Claudio Redi on 1/20/2016.
 */
public interface ITrackGroupDeserializer {
    TrackGroup deserialize(String jsonString) throws JSONException;
}
