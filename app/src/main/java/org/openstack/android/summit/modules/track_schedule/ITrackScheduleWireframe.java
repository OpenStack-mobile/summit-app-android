package org.openstack.android.summit.modules.track_schedule;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.IScheduleWireframe;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.track_schedule.user_interface.ITrackScheduleView;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public interface ITrackScheduleWireframe extends IScheduleWireframe {
    void presentTrackScheduleView(NamedDTO track, IBaseView context);

    void reloadTrackScheduleView(NamedDTO track, IBaseView context);

    void showFilterView(IBaseView view);
}
