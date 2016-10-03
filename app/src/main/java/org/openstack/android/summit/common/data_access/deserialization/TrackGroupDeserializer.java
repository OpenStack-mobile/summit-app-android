package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.SummitType;
import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.entities.TrackGroup;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.utils.RealmFactory;

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
        int groupId           = jsonObject.getInt("id");
        TrackGroup trackGroup = deserializerStorage.exist(groupId, TrackGroup.class) ?
                deserializerStorage.get(groupId, TrackGroup.class) :
                new TrackGroup();

        deserializerStorage.add(trackGroup, TrackGroup.class); // added here so it's available on child deserialization

        trackDeserializer.setShouldDeserializeTrackGroups(false);

        trackGroup.setId(groupId);
        trackGroup.setName(jsonObject.getString("name"));
        trackGroup.setDescription(jsonObject.getString("description"));
        trackGroup.setColor(jsonObject.getString("color"));

        Track track               = null;
        JSONArray jsonArrayTracks = jsonObject.getJSONArray("tracks");
        trackGroup.getTracks().clear();

        for (int i = 0; i < jsonArrayTracks.length(); i++) {

            track =  (jsonArrayTracks.optInt(i) > 0) ?
                    deserializerStorage.get(jsonArrayTracks.optInt(i), Track.class):
                    trackDeserializer.deserialize(jsonArrayTracks.getJSONObject(i).toString());

            if(track == null) continue;

            track.getTrackGroups().add(trackGroup);
            trackGroup.getTracks().add(track);
        }

        return trackGroup;
    }
}
