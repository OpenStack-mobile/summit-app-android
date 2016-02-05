package org.openstack.android.summit.modules.track_list.user_interface;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.TrackDTO;
import org.openstack.android.summit.common.user_interface.IBaseView;

import java.util.List;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public interface ITrackListView extends IBaseView {
    void setTracks(List<TrackDTO> tracks);
}
