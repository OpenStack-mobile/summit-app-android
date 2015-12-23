package org.openstack.android.summit.modules.events;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;

import org.openstack.android.summit.R;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.events.user_interface.EventsFragment;

import javax.inject.Inject;

/**
 * Created by claudio on 10/30/2015.
 */
public class EventsWireframe implements IEventsWireframe {

    @Override
    public void presentEventsView(FragmentActivity context) {
        EventsFragment eventsFragment = new EventsFragment();
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout_content, eventsFragment)
                .addToBackStack(null)
                .commit();
    }
}
