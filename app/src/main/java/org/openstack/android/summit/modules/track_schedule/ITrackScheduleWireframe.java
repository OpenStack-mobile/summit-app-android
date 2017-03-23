package org.openstack.android.summit.modules.track_schedule;

import org.openstack.android.summit.common.IScheduleWireframe;
import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public interface ITrackScheduleWireframe extends IScheduleWireframe {

    void presentTrackScheduleView(int trackId, IBaseView context);

    void reloadTrackScheduleView(int trackId, IBaseView context);

    void showFilterView(IBaseView view);
}
