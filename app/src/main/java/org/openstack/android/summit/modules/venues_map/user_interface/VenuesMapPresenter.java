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

    public VenuesMapPresenter(IVenuesMapInteractor interactor, IVenuesMapWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        venues = interactor.getVenues();
        view.addMarkers(venues);

        super.onCreateView(savedInstanceState);
    }

    @Override
    public void showVenueDetail(int venueId) {
        for (VenueListItemDTO venue: venues) {
            if (venue.getId() == venueId) {
                wireframe.showVenueDetailView(venue, view);
            }
        }
    }
}
