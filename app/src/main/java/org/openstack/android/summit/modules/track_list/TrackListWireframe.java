package org.openstack.android.summit.modules.track_list;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.track_schedule.ITrackScheduleWireframe;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public class TrackListWireframe implements ITrackListWireframe {

    private ITrackScheduleWireframe trackScheduleWireframe;

    public TrackListWireframe(ITrackScheduleWireframe trackScheduleWireframe) {
        this.trackScheduleWireframe = trackScheduleWireframe;
    }

    @Override
    public void showTrackSchedule(NamedDTO track, IBaseView context) {
        trackScheduleWireframe.presentTrackScheduleView(track, context);
    }
}
