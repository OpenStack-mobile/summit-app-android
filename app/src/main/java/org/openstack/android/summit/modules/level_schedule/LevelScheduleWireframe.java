package org.openstack.android.summit.modules.level_schedule;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.ScheduleWireframe;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.event_detail.user_interface.EventDetailFragment;
import org.openstack.android.summit.modules.level_schedule.user_interface.LevelScheduleFragment;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public class LevelScheduleWireframe extends ScheduleWireframe implements ILevelScheduleWireframe {
    public LevelScheduleWireframe(IEventDetailWireframe eventDetailWireframe) {
        super(eventDetailWireframe);
    }

    @Override
    public void presentLevelScheduleView(String level, FragmentActivity context) {
        LevelScheduleFragment levelScheduleFragment = new LevelScheduleFragment();
        levelScheduleFragment.setLevel(level);
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout_content, levelScheduleFragment)
                .addToBackStack(null)
                .commit();

    }
}
