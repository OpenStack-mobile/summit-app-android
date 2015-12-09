package org.openstack.android.summit.modules.events;

import android.app.Activity;
import android.app.FragmentManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.events.user_interface.EventsFragment;

import javax.inject.Inject;

/**
 * Created by claudio on 10/30/2015.
 */
public class EventsWireframe implements IEventsWireframe {

    IEventDetailWireframe eventDetailWireframe;

    @Inject
    public EventsWireframe(IEventDetailWireframe eventDetailWireframe) {
        this.eventDetailWireframe = eventDetailWireframe;
    }

    @Override
    public void showEventDetail(Activity context) {
        eventDetailWireframe.presentEventDetailView(context);
    }

    @Override
    public void presentGeneralScheduleView(Activity context) {
        EventsFragment generalScheduleFragment = new EventsFragment();
        FragmentManager fragmentManager = context.getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout_content, generalScheduleFragment)
                .addToBackStack(null)
                .commit();
    }
}
