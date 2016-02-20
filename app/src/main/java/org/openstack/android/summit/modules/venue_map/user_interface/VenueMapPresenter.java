package org.openstack.android.summit.modules.venue_map.user_interface;

import android.os.Bundle;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.VenueListItemDTO;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.venue_map.IVenueMapWireframe;
import org.openstack.android.summit.modules.venue_map.business_logic.IVenueMapInteractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenueMapPresenter extends BasePresenter<IVenueMapView, IVenueMapInteractor, IVenueMapWireframe> implements IVenueMapPresenter {
    VenueListItemDTO venue;
    Integer venueId;

    public VenueMapPresenter(IVenueMapInteractor interactor, IVenueMapWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            venueId = savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_VENUE);
        }
        else {
            venueId = wireframe.getParameter(Constants.NAVIGATION_PARAMETER_VENUE, Integer.class);
        }

        if (venueId != null) {
            venue = interactor.getVenue(venueId);
        }
        view.setMarker(venue);

        super.onCreateView(savedInstanceState);
    }
}
