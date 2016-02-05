package org.openstack.android.summit.modules.track_list.business_logic;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.entities.Track;

import java.util.List;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public interface ITrackListInteractor {
    List<NamedDTO> getTracks(List<Integer> trackGroups);
}
