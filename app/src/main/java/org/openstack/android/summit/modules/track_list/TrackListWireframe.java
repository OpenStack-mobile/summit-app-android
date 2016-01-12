package org.openstack.android.summit.modules.track_list;

import android.support.v4.app.FragmentActivity;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.modules.track_schedule.ITrackScheduleWireframe;
import org.openstack.android.summit.modules.track_schedule.user_interface.TrackScheduleFragment;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public class TrackListWireframe implements ITrackListWireframe {

    private ITrackScheduleWireframe trackScheduleWireframe;

    public TrackListWireframe(ITrackScheduleWireframe trackScheduleWireframe) {
        this.trackScheduleWireframe = trackScheduleWireframe;
    }

    @Override
    public void showTrackSchedule(NamedDTO track, FragmentActivity context) {
        trackScheduleWireframe.presentTrackScheduleView(track, context);
    }
}
