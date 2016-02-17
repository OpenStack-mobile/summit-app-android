package org.openstack.android.summit.modules.venues_map;

import android.support.v4.app.FragmentManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.BaseWireframe;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.VenueDTO;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.venue_detail.user_interface.VenueDetailFragment;
import org.openstack.android.summit.modules.venues_map.user_interface.VenuesMapFragment;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenuesMapWireframe extends BaseWireframe implements IVenuesMapWireframe {
    public VenuesMapWireframe(INavigationParametersStore navigationParametersStore) {
        super(navigationParametersStore);
    }

    @Override
    public void presentVenueMapView(VenueDTO venue, IBaseView context) {
        VenuesMapFragment venuesMapFragment = new VenuesMapFragment();
        navigationParametersStore.put(Constants.NAVIGATION_PARAMETER_VENUE, venue.getId());
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout_content, venuesMapFragment)
                .addToBackStack(null)
                .commit();
    }
}
