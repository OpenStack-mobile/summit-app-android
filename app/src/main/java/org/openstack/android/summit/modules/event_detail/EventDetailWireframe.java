package org.openstack.android.summit.modules.event_detail;

import android.app.Activity;
import android.app.FragmentManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.modules.event_detail.user_interface.EventDetailFragment;

import javax.inject.Inject;

/**
 * Created by claudio on 11/2/2015.
 */
public class EventDetailWireframe implements IEventDetailWireframe {

    @Inject
    public EventDetailWireframe() {

    }

    @Override
    public void presentEventDetailView(Activity context) {
        EventDetailFragment eventDetailFragment = new EventDetailFragment();
        FragmentManager fragmentManager = context.getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout_content, eventDetailFragment)
                .addToBackStack(null)
                .commit();
    }
}
