package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.SummitType;
import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.entities.TrackGroup;
import org.openstack.android.summit.common.entities.Venue;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/20/2016.
 */
public class TrackGroupDeserializer extends BaseDeserializer implements ITrackGroupDeserializer {
    IDeserializerStorage deserializerStorage;
    ITrackDeserializer trackDeserializer;

    @Inject
    public TrackGroupDeserializer(IDeserializerStorage deserializerStorage, ITrackDeserializer trackDeserializer){
        this.deserializerStorage = deserializerStorage;
        this.trackDeserializer = trackDeserializer;
    }

    @Override
    public TrackGroup deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[] {"id", "name", "color", "tracks"},  jsonObject);
        handleMissedFieldsIfAny(missedFields);

        TrackGroup trackGroup = new TrackGroup();
        deserializerStorage.add(trackGroup, TrackGroup.class); // added here so it's available on child deserialization

        trackGroup.setId(jsonObject.getInt("id"));
        trackGroup.setName(jsonObject.getString("name"));
        trackGroup.setDescription(jsonObject.getString("description"));
        trackGroup.setColor(jsonObject.getString("color"));

        if(!deserializerStorage.exist(trackGroup, TrackGroup.class)) {
            deserializerStorage.add(trackGroup, TrackGroup.class);
        }

        Track track = null;
        int trackId = 0;
        JSONArray jsonArrayTracks = jsonObject.getJSONArray("tracks");
        for (int i = 0; i < jsonArrayTracks.length(); i++) {

            if (jsonArrayTracks.optInt(i) > 0) {
                trackId = jsonArrayTracks.getInt(i);
                track = deserializerStorage.get(trackId, Track.class);
            }
            else {
                track = trackDeserializer.deserialize(jsonArrayTracks.getJSONObject(i).toString());
            }
            track.getTrackGroups().add(trackGroup);
            trackGroup.getTracks().add(track);
        }

        return trackGroup;
    }
}
