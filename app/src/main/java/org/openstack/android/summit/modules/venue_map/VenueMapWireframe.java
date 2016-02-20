package org.openstack.android.summit.modules.venue_map;

import android.support.v4.app.FragmentManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.BaseWireframe;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.VenueDTO;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.venue_map.user_interface.VenueMapFragment;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenueMapWireframe extends BaseWireframe implements IVenueMapWireframe {

    public VenueMapWireframe(INavigationParametersStore navigationParametersStore) {
        super(navigationParametersStore);
    }

    @Override
    public void presentVenueMapView(VenueDTO venue, IBaseView context) {
        VenueMapFragment venueMapFragment = new VenueMapFragment();
        navigationParametersStore.put(Constants.NAVIGATION_PARAMETER_VENUE, venue.getId());
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout_content, venueMapFragment)
                .addToBackStack(null)
                .commit();
    }
}
