package org.openstack.android.summit.common.data_access.repositories.impl;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.data_access.repositories.ITrackGroupDataStore;
import org.openstack.android.summit.common.data_access.repositories.strategies.IDeleteStrategy;
import org.openstack.android.summit.common.data_access.repositories.strategies.ISaveOrUpdateStrategy;
import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.entities.TrackGroup;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import java.util.List;

import io.realm.Realm;


/**
 * Created by Claudio Redi on 2/25/2016.
 */
public class TrackGroupDataStore extends GenericDataStore<TrackGroup> implements ITrackGroupDataStore {

    public TrackGroupDataStore(ISaveOrUpdateStrategy saveOrUpdateStrategy, IDeleteStrategy deleteStrategy) {
        super(TrackGroup.class, saveOrUpdateStrategy, deleteStrategy);
    }

    @Override
    public List<TrackGroup> getTrackGroupsForTrack(String trackName) {
        return RealmFactory.getSession().where(TrackGroup.class).equalTo("tracks.name", trackName).findAll();
    }

    @Override
    public List<Track> getTracks(int trackGroupId) {
        return RealmFactory.getSession().where(Track.class).equalTo("trackGroups.id", trackGroupId).findAll();
    }

    @Override
    public void removeTrackGroupFromTracks(final int trackGroupId) {
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

    @Override
    public List<TrackGroup> getAllBySummit(int summitId) {
        return RealmFactory.getSession().where(TrackGroup.class).equalTo("summit.id", summitId).findAllSorted("name");
    }

}
