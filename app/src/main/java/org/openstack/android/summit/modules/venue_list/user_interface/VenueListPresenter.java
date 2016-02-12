package org.openstack.android.summit.modules.venue_list.user_interface;

import android.os.Bundle;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.VenueListItemDTO;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.common.user_interface.SimpleListItemView;
import org.openstack.android.summit.modules.venue_list.IVenueListWireframe;
import org.openstack.android.summit.modules.venue_list.business_logic.IVenueListInteractor;

import java.util.List;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenueListPresenter extends BasePresenter<IVenueListView, IVenueListInteractor, IVenueListWireframe> implements IVenueListPresenter {
    List<NamedDTO> venues;

    public VenueListPresenter(IVenueListInteractor interactor, IVenueListWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        venues = interactor.getVenues();
        view.setVenues(venues);
    }

    @Override
    public void showVenueDetail(int position) {
        NamedDTO venue = venues.get(position);
        wireframe.showVenueDetail(venue, view);
    }

    @Override
    public void buildItem(IVenueListItemView venueListItemView, int position) {
        NamedDTO venue = venues.get(position);
        venueListItemView.setName(venue.getName());
    }
}
