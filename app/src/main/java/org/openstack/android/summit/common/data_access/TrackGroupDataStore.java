package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.entities.TrackGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Claudio Redi on 2/25/2016.
 */
public class TrackGroupDataStore extends GenericDataStore implements ITrackGroupDataStore {
    @Override
    public List<TrackGroup> getTrackGroupsForTrack(String trackName) {
        RealmResults<TrackGroup> trackGroups = realm.where(TrackGroup.class).equalTo("tracks.name", trackName).findAll();
        return trackGroups;
    }

    @Override
    public List<Track> getTracks(int trackGroupId) {
        RealmResults<Track> tracks = realm.where(Track.class).equalTo("trackGroups.id", trackGroupId).findAll();
        return tracks;
    }

    @Override
    public void removeTrackGroupFromTracksLocal(int trackGroupId) {
        TrackGroup trackGroup = realm.where(TrackGroup.class).equalTo("id", trackGroupId).findFirst();
        if (trackGroup != null) {
            List<Track> results = getTracks(trackGroupId);
            try {
                List<Track> tracks = new ArrayList<>();
                tracks.addAll(results);
                realm.beginTransaction();
                for (Track track : tracks) {
                    track.getTrackGroups().remove(trackGroup);
                }
                realm.commitTransaction();
            }
            catch (Exception e) {
                realm.cancelTransaction();
                throw e;
            }
        }
    }
}
