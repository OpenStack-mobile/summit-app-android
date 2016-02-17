package org.openstack.android.summit.modules.venues_map.user_interface;

import android.os.Bundle;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.VenueListItemDTO;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.venues_map.IVenuesMapWireframe;
import org.openstack.android.summit.modules.venues_map.business_logic.IVenuesMapInteractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenuesMapPresenter extends BasePresenter<IVenuesMapView, IVenuesMapInteractor, IVenuesMapWireframe> implements IVenuesMapPresenter {
    List<VenueListItemDTO> venues;
    Integer venueId;

    public VenuesMapPresenter(IVenuesMapInteractor interactor, IVenuesMapWireframe wireframe) {
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
            venues = new ArrayList<>();
            venues.add(interactor.getVenue(venueId));
        }
        else {
            venues = interactor.getVenues();
        }
        view.addMarkers(venues);

        super.onCreateView(savedInstanceState);
    }
}
