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
    private VenueListItemDTO venue;
    private Integer venueId;

    public VenueMapPresenter(IVenueMapInteractor interactor, IVenueMapWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        venueId = (savedInstanceState != null) ?
                savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_VENUE, 0):
                wireframe.getParameter(Constants.NAVIGATION_PARAMETER_VENUE, Integer.class);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        if (venueId != null) {
            venue = interactor.getVenue(venueId);
            view.setMarker(venue);
        }
        super.onCreateView(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(venueId != null){
            outState.putInt(Constants.NAVIGATION_PARAMETER_VENUE, venueId);
        }
    }
}
