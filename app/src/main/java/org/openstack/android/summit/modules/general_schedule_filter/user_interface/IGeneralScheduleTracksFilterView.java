package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

import org.openstack.android.summit.common.DTOs.TrackDTO;
import org.openstack.android.summit.common.user_interface.IBaseView;
import java.util.List;

/**
 * Created by smarcet on 2/5/18.
 */

public interface IGeneralScheduleTracksFilterView extends IBaseView {

    void showTracks(List<TrackDTO> track);

    Integer getSelectedTrackGroupId();
}
