package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.entities.TrackGroup;
import org.openstack.android.summit.common.utils.RealmFactory;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/20/2016.
 */
public class TrackGroupDeserializer extends BaseDeserializer implements ITrackGroupDeserializer {

    ITrackDeserializer trackDeserializer;

    @Inject
    public TrackGroupDeserializer(ITrackDeserializer trackDeserializer){
        this.trackDeserializer = trackDeserializer;
    }

    @Override
    public TrackGroup deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[] {"id", "name", "color", "tracks"},  jsonObject);
        handleMissedFieldsIfAny(missedFields);
        int groupId           = jsonObject.getInt("id");

        TrackGroup trackGroup = RealmFactory.getSession().where(TrackGroup.class).equalTo("id", groupId).findFirst();
        if(trackGroup == null)
            trackGroup = RealmFactory.getSession().createObject(TrackGroup.class, groupId);



        trackGroup.setName(jsonObject.getString("name"));
        trackGroup.setDescription(jsonObject.getString("description"));
        trackGroup.setColor(jsonObject.getString("color"));

        Track track               = null;
        JSONArray jsonArrayTracks = jsonObject.getJSONArray("tracks");
        trackGroup.getTracks().clear();

        trackDeserializer.setShouldDeserializeTrackGroups(false);
        for (int i = 0; i < jsonArrayTracks.length(); i++) {
            int trackId = jsonArrayTracks.optInt(i);
            if(trackId == 0 ) trackId = jsonArrayTracks.getJSONObject(i).getInt("id");
            track =  trackId > 0  ?
                    RealmFactory.getSession().where(Track.class).equalTo("id", trackId).findFirst() :
                    trackDeserializer.deserialize(jsonArrayTracks.getJSONObject(i).toString());

            if(track == null) continue;
            trackGroup.getTracks().add(track);
            if(!track.getTrackGroups().contains(trackGroup))
                track.getTrackGroups().add(trackGroup);
        }

        return trackGroup;
    }
}
