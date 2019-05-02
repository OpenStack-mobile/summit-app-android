package org.openstack.android.summit.modules.venue_detail;

import androidx.fragment.app.FragmentManager;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.BaseWireframe;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.VenueDTO;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.event_detail.user_interface.VenueRoomDetailFragment;
import org.openstack.android.summit.modules.venue_detail.user_interface.VenueDetailFragment;
import org.openstack.android.summit.modules.venue_map.IVenueMapWireframe;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public class VenueDetailWireframe extends BaseWireframe implements IVenueDetailWireframe {
    IVenueMapWireframe venueMapWireframe;

    public VenueDetailWireframe(IVenueMapWireframe venueMapWireframe, INavigationParametersStore navigationParametersStore) {
        super(navigationParametersStore);
        this.venueMapWireframe = venueMapWireframe;
    }

    public void presentVenueDetailView(NamedDTO venue, IBaseView context) {
        try {
            VenueDetailFragment venueDetailFragment = new VenueDetailFragment();
            navigationParametersStore.put(Constants.NAVIGATION_PARAMETER_VENUE, venue.getId());
            navigationParametersStore.remove(Constants.NAVIGATION_PARAMETER_ROOM);
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
                    .replace(R.id.frame_layout_content, venueDetailFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }
        catch(Exception ex){
            Crashlytics.logException(ex);
        }
    }

    public void presentLocationDetailView(NamedDTO location, IBaseView context) {
        try {
            VenueRoomDetailFragment locationDetailFragment = new VenueRoomDetailFragment();
            navigationParametersStore.put(Constants.NAVIGATION_PARAMETER_ROOM, location.getId());
            navigationParametersStore.remove(Constants.NAVIGATION_PARAMETER_VENUE);
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
                    .replace(R.id.frame_layout_content, locationDetailFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }
        catch(Exception ex){
            Crashlytics.logException(ex);
        }
    }

    public void showVenueMapView(VenueDTO venueDTO, IBaseView context) {
        venueMapWireframe.presentVenueMapView(venueDTO, context);
    }
}
