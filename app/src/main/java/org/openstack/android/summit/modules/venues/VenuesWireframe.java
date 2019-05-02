package org.openstack.android.summit.modules.venues;

import androidx.fragment.app.FragmentManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.venues.user_interface.VenuesFragment;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenuesWireframe implements IVenuesWireframe {
    public void presentVenuesView(IBaseView context) {
        VenuesFragment venuesFragment = new VenuesFragment();
        FragmentManager fragmentManager = context.getSupportFragmentManager();

        fragmentManager
                .beginTransaction()
                .setCustomAnimations
                        (
                                R.anim.slide_in_left,
                                R.anim.slide_out_left,
                                R.anim.slide_out_right,
                                R.anim.slide_in_right
                        )
                .replace(R.id.frame_layout_content, venuesFragment, "nav_venues")
                .addToBackStack("nav_venues")
                .commitAllowingStateLoss();
    }
}
