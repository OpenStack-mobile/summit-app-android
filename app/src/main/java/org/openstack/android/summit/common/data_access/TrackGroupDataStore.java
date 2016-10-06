package org.openstack.android.summit.common.data_access;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.entities.TrackGroup;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Claudio Redi on 2/25/2016.
 */
public class TrackGroupDataStore extends GenericDataStore implements ITrackGroupDataStore {
    @Override
    public List<TrackGroup> getTrackGroupsForTrack(String trackName) {
        RealmResults<TrackGroup> trackGroups = RealmFactory.getSession().where(TrackGroup.class).equalTo("tracks.name", trackName).findAll();
        return trackGroups;
    }

    @Override
    public List<Track> getTracks(int trackGroupId) {
        RealmResults<Track> tracks = RealmFactory.getSession().where(Track.class).equalTo("trackGroups.id", trackGroupId).findAll();
        return tracks;
    }

    @Override
    public void removeTrackGroupFromTracksLocal(final int trackGroupId) {
        try {
            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception {
                    TrackGroup trackGroup = session.where(TrackGroup.class).equalTo("id", trackGroupId).findFirst();
                    if (trackGroup != null) {
                        for (Track track : trackGroup.getTracks()) {
                            track.getTrackGroups().remove(trackGroup);
                        }
                        trackGroup.getTracks().clear();
                    }
                    return Void.getInstance();
                }
            });
        }
        catch(Exception ex){
            Log.e(Constants.LOG_TAG, ex.getMessage());
            Crashlytics.logException(ex);
        }
    }
}
