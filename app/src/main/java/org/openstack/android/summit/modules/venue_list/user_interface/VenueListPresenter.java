package org.openstack.android.summit.modules.venue_list.user_interface;

import android.net.Uri;
import android.os.Bundle;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.VenueDTO;
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
    List<VenueDTO> internalVenues;
    List<VenueDTO> externalVenues;

    public VenueListPresenter(IVenueListInteractor interactor, IVenueListWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        internalVenues = interactor.getInternalVenues();
        externalVenues = interactor.getExternalVenues();
        view.setInternalVenues(internalVenues);
        view.setExternalVenues(externalVenues);
    }

    @Override
    public void showInternalVenueDetail(int position) {
        NamedDTO venue = internalVenues.get(position);
        wireframe.showVenueDetail(venue, view);
    }

    @Override
    public void buildInternalVenueItem(IVenueListItemView venueListItemView, int position) {
        VenueDTO venue = internalVenues.get(position);
        venueListItemView.setName(venue.getName());
        venueListItemView.setAddress(venue.getAddress());
        String imageUrl = venue.getImages().size() > 0 ? venue.getImages().get(0) : null;

        if (imageUrl != null) {
            venueListItemView.setPictureUri(Uri.parse(imageUrl));
        }
    }

    @Override
    public void showExternalVenueDetail(int position) {
        NamedDTO venue = externalVenues.get(position);
        wireframe.showVenueDetail(venue, view);
    }

    @Override
    public void buildExternalVenueItem(IVenueListItemView venueListItemView, int position) {
        VenueDTO venue = externalVenues.get(position);
        venueListItemView.setName(venue.getName());
        venueListItemView.setAddress(venue.getAddress());
    }
}
