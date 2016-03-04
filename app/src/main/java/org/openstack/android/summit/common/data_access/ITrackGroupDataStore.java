package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.entities.TrackGroup;

import java.util.List;

/**
 * Created by Claudio Redi on 2/25/2016.
 */
public interface ITrackGroupDataStore extends IGenericDataStore {
    List<TrackGroup> getTrackGroupsForTrack(String trackName);

    List<Track> getTracks(int id);
}
