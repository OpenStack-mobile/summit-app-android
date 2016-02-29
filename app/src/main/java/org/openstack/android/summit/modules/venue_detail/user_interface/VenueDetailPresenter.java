package org.openstack.android.summit.modules.venue_detail.user_interface;

import android.os.Bundle;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.VenueDTO;
import org.openstack.android.summit.common.DTOs.VenueRoomDTO;
import org.openstack.android.summit.common.business_logic.IBaseInteractor;
import org.openstack.android.summit.common.entities.VenueRoom;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.common.user_interface.ISimpleListItemView;
import org.openstack.android.summit.modules.venue_detail.IVenueDetailWireframe;
import org.openstack.android.summit.modules.venue_detail.business_logic.IVenueDetailInteractor;

import java.util.List;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public class VenueDetailPresenter extends BasePresenter<IVenueDetailView, IVenueDetailInteractor, IVenueDetailWireframe> implements IVenueDetailPresenter {
    VenueDTO venue;
    List<VenueRoomDTO> venueRooms;

    public VenueDetailPresenter(IVenueDetailInteractor interactor, IVenueDetailWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        int venueId = 0;
        if (savedInstanceState != null) {
            venueId = savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_VENUE);
        }
        else {
            venueId = wireframe.getParameter(Constants.NAVIGATION_PARAMETER_VENUE, Integer.class);
        }
        venue = interactor.getVenue(venueId);
        view.setVenueName(venue.getName());
        view.setLocation(venue.getAddress());

        view.toggleImagesGallery(venue.getImages().size() > 0);
        if (venue.getImages().size() > 0) {
            view.setImages(venue.getImages());
        }

        view.toggleMapNavigation(venue.getMaps().size() > 0);
        view.toggleMapsGallery(venue.getMaps().size() > 0);
        view.toggleMap(venue.getMaps().size() == 0);

        if (venue.getMaps().size() > 0) {
            view.setMaps(venue.getMaps());
        } else {
            view.setMarker(venue);
        }
    }

    @Override
    public void buildItem(ISimpleListItemView venueRoomListItem, int position) {
        VenueRoomDTO venueRoomDTO = venueRooms.get(position);
        venueRoomListItem.setName(venueRoomDTO.getName());
        venueRoomListItem.allowNavigation(false);
    }

    @Override
    public void showToMapIfApplies() {
        if (venue.getMaps().size() > 0) {
            wireframe.showVenueMapView(venue, view);
        }
    }
}
