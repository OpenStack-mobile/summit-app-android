package org.openstack.android.summit.modules.level_schedule;

import android.support.v4.app.FragmentActivity;

import org.openstack.android.summit.common.IScheduleWireframe;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public interface ILevelScheduleWireframe extends IScheduleWireframe {
    void presentLevelScheduleView(String level, FragmentActivity context);
}
