package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.util.List;

import io.realm.Sort;

/**
 * Created by smarcet on 12/7/16.
 */

public class TrackDataStore extends GenericDataStore implements ITrackDataStore {
    @Override
    public List<Track> getAllOrderedByName(int summitId) {
        return RealmFactory.getSession().where(Track.class).equalTo("summit.id", summitId).findAllSorted("name", Sort.ASCENDING);
    }
}
