package org.openstack.android.summit.modules.venues;

import android.support.v4.app.FragmentManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.events.user_interface.EventsFragment;
import org.openstack.android.summit.modules.venues.user_interface.VenuesFragment;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenuesWireframe implements IVenuesWireframe {
    public void presentVenuesView(IBaseView context) {
        VenuesFragment venuesFragment = new VenuesFragment();
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout_content, venuesFragment)
                .commit();
    }
}
