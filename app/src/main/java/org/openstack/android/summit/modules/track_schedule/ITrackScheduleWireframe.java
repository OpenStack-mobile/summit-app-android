package org.openstack.android.summit.modules.track_schedule;

import android.support.v4.app.FragmentActivity;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.IScheduleWireframe;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public interface ITrackScheduleWireframe extends IScheduleWireframe {
    void presentTrackScheduleView(NamedDTO track, FragmentActivity context);
}
