package org.openstack.android.summit.modules.level_list;

import android.support.v4.app.FragmentActivity;

import org.openstack.android.summit.modules.level_schedule.ILevelScheduleWireframe;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public class LevelListWireframe implements ILevelListWireframe {

    private ILevelScheduleWireframe levelScheduleWireframe;

    public LevelListWireframe(ILevelScheduleWireframe levelScheduleWireframe) {
        this.levelScheduleWireframe = levelScheduleWireframe;
    }

    @Override
    public void showLevelSchedule(String level, FragmentActivity context) {
        levelScheduleWireframe.presentLevelScheduleView(level, context);
    }
}
