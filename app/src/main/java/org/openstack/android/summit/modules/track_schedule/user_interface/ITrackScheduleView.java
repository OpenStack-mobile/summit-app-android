package org.openstack.android.summit.modules.track_schedule.user_interface;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.user_interface.IScheduleView;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public interface ITrackScheduleView extends IScheduleView {
    void setTrack(String title);

    void setShowActiveFilterIndicator(boolean showActiveFilterIndicator);
}
