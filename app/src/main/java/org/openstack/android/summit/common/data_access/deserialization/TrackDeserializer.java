package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.entities.TrackGroup;
import org.openstack.android.summit.common.utils.RealmFactory;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 2/9/2016.
 */
public class TrackDeserializer extends BaseDeserializer implements ITrackDeserializer {
    private boolean shouldDeserializeTrackGroups = true;

    @Inject
    public TrackDeserializer(){

    }

    @Override
    public Track deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[] {"id", "name", "track_groups"},  jsonObject);
        handleMissedFieldsIfAny(missedFields);

        int trackId = jsonObject.getInt("id");

        Track track = RealmFactory.getSession().where(Track.class).equalTo("id", trackId).findFirst();
        if(track == null)
            track = RealmFactory.getSession().createObject(Track.class, trackId);

        track.setName(jsonObject.getString("name"));
        track.getTrackGroups().clear();

        if (shouldDeserializeTrackGroups) {
            int trackGroupId;
            TrackGroup trackGroup;
            JSONArray jsonArrayTrackGroups = jsonObject.getJSONArray("track_groups");

            for (int i = 0; i < jsonArrayTrackGroups.length(); i++) {
                trackGroupId = jsonArrayTrackGroups.getInt(i);
                trackGroup   = RealmFactory.getSession().where(TrackGroup.class).equalTo("id", trackGroupId).findFirst();
                if(trackGroup != null)
                    track.getTrackGroups().add(trackGroup);
            }
        }

        return track;
    }

    public boolean getShouldDeserializeTrackGroups() {
        return shouldDeserializeTrackGroups;
    }

    public void setShouldDeserializeTrackGroups(boolean shouldDeserializeTrackGroups) {
        this.shouldDeserializeTrackGroups = shouldDeserializeTrackGroups;
    }
}
