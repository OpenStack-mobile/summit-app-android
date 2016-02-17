package org.openstack.android.summit.modules.venue_detail;

import android.support.v4.app.FragmentManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.BaseWireframe;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.VenueDTO;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.track_schedule.user_interface.TrackScheduleFragment;
import org.openstack.android.summit.modules.venue_detail.user_interface.VenueDetailFragment;
import org.openstack.android.summit.modules.venues_map.IVenuesMapWireframe;
import org.openstack.android.summit.modules.venues_map.VenuesMapWireframe;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public class VenueDetailWireframe extends BaseWireframe implements IVenueDetailWireframe {
    IVenuesMapWireframe venuesMapWireframe;

    public VenueDetailWireframe(IVenuesMapWireframe venuesMapWireframe, INavigationParametersStore navigationParametersStore) {
        super(navigationParametersStore);
        this.venuesMapWireframe = venuesMapWireframe;
    }

    public void presentVenueDetailView(NamedDTO venue, IBaseView context) {
        VenueDetailFragment venueDetailFragment = new VenueDetailFragment();
        navigationParametersStore.put(Constants.NAVIGATION_PARAMETER_VENUE, venue.getId());
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout_content, venueDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    public void showVenueMapView(VenueDTO venueDTO, IBaseView context) {
        venuesMapWireframe.presentVenueMapView(venueDTO, context);
    }
}
