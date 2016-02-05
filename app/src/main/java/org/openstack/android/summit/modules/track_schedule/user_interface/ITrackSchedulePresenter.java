package org.openstack.android.summit.modules.track_schedule.user_interface;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.user_interface.ISchedulePresenter;
import org.openstack.android.summit.modules.track_schedule.ITrackScheduleWireframe;
import org.openstack.android.summit.modules.track_schedule.business_logic.ITrackScheduleInteractor;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public interface ITrackSchedulePresenter extends ISchedulePresenter<ITrackScheduleView> {
    void showFilterView();

    void clearFilters();
}
