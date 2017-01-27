package org.openstack.android.summit.common.data_access.repositories;

import org.openstack.android.summit.common.entities.Track;

import java.util.List;

/**
 * Created by smarcet on 12/7/16.
 */

public interface ITrackDataStore extends IGenericDataStore<Track> {
    List<Track> getAllOrderedByName(int summitId);
}
