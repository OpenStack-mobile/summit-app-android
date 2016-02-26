package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.entities.TrackGroup;

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
    public List<Track> getTracks(int id) {
        RealmResults<Track> tracks = realm.where(Track.class).equalTo("trackGroups.id", id).findAll();
        return tracks;
    }
}
