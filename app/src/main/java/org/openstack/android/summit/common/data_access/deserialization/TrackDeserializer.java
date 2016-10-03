package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.entities.TrackGroup;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.utils.RealmFactory;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 2/9/2016.
 */
public class TrackDeserializer extends BaseDeserializer implements ITrackDeserializer {
    private IDeserializerStorage deserializerStorage;
    private boolean shouldDeserializeTrackGroups = true;

    @Inject
    public TrackDeserializer(IDeserializerStorage deserializerStorage){
        this.deserializerStorage = deserializerStorage;
    }

    @Override
    public Track deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[] {"id", "name", "track_groups"},  jsonObject);
        handleMissedFieldsIfAny(missedFields);

        int trackId = jsonObject.getInt("id");
        Track track = deserializerStorage.exist(trackId, Track.class) ?
                      deserializerStorage.get(trackId, Track.class) :
                      new Track();

        track.setId(trackId);
        track.setName(jsonObject.getString("name"));
        track.getTrackGroups().clear();

        if (shouldDeserializeTrackGroups) {
            int trackGroupId;
            TrackGroup trackGroup;
            JSONArray jsonArrayTrackGroups = jsonObject.getJSONArray("track_groups");

            for (int i = 0; i < jsonArrayTrackGroups.length(); i++) {
                trackGroupId = jsonArrayTrackGroups.getInt(i);
                //first check db, and then cache storage
                trackGroup   = RealmFactory.getSession().where(TrackGroup.class).equalTo("id", trackGroupId).findFirst();
                if(trackGroup == null)
                    trackGroup = deserializerStorage.get(trackGroupId, TrackGroup.class);
                if(trackGroup != null)
                    track.getTrackGroups().add(trackGroup);
            }
        }

        if(!deserializerStorage.exist(track, Track.class)) {
            deserializerStorage.add(track, Track.class);
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
