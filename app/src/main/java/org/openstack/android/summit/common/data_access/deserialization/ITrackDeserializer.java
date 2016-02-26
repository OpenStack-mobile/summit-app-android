package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.openstack.android.summit.common.entities.Track;

/**
 * Created by Claudio Redi on 2/9/2016.
 */
public interface ITrackDeserializer {
    Track deserialize(String jsonString) throws JSONException;

    boolean getShouldDeserializeTrackGroups();

    void setShouldDeserializeTrackGroups(boolean shouldDeserializeTrackGroups);
}
