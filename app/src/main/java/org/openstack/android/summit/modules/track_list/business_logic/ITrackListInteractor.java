package org.openstack.android.summit.modules.track_list.business_logic;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.TrackDTO;
import org.openstack.android.summit.common.business_logic.IBaseInteractor;
import org.openstack.android.summit.common.entities.Track;

import java.util.List;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public interface ITrackListInteractor extends IBaseInteractor {
    List<TrackDTO> getTracks(List<Integer> trackGroups);

    List<TrackDTO> getTracksById(List<Integer> trackIds);
}
