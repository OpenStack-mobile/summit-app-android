package org.openstack.android.openstacksummit.modules.general_schedule;

import android.app.Activity;
import android.app.FragmentManager;
import android.support.v4.app.Fragment;

import org.openstack.android.openstacksummit.R;
import org.openstack.android.openstacksummit.modules.event_detail.EventDetailWireframe;
import org.openstack.android.openstacksummit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.openstacksummit.modules.event_detail.user_interface.EventDetailFragment;
import org.openstack.android.openstacksummit.modules.general_schedule.user_interface.GeneralScheduleFragment;

import javax.inject.Inject;

/**
 * Created by claudio on 10/30/2015.
 */
public class GeneralScheduleWireframe implements IGeneralScheduleWireframe {

    IEventDetailWireframe eventDetailWireframe;

    @Inject
    public GeneralScheduleWireframe(IEventDetailWireframe eventDetailWireframe) {
        this.eventDetailWireframe = eventDetailWireframe;
    }

    @Override
    public void showEventDetail(Activity context) {
        eventDetailWireframe.presentEventDetailView(context);
    }

    @Override
    public void presentGeneralScheduleView(Activity context) {
        GeneralScheduleFragment generalScheduleFragment = new GeneralScheduleFragment();
        FragmentManager fragmentManager = context.getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout_content, generalScheduleFragment)
                .addToBackStack(null)
                .commit();
    }
}
