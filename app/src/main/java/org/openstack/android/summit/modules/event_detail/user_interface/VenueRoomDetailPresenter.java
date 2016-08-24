package org.openstack.android.summit.modules.event_detail.user_interface;

import android.os.Bundle;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.VenueFloorDTO;
import org.openstack.android.summit.common.DTOs.VenueRoomDTO;
import org.openstack.android.summit.modules.venue_detail.IVenueDetailWireframe;
import org.openstack.android.summit.modules.venue_detail.business_logic.IVenueDetailInteractor;
import org.openstack.android.summit.modules.venue_detail.user_interface.VenueDetailPresenter;
import java.util.List;

/**
 * Created by sebastian on 8/9/2016.
 */
public class VenueRoomDetailPresenter extends VenueDetailPresenter implements IVenueRoomDetailPresenter {

    private VenueRoomDTO room;
    private VenueFloorDTO floor;

    public VenueRoomDetailPresenter(IVenueDetailInteractor interactor, IVenueDetailWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {

        int locationId = (savedInstanceState != null) ?
                          savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_ROOM):
                          wireframe.getParameter(Constants.NAVIGATION_PARAMETER_ROOM, Integer.class);

        room  = interactor.getRoom(locationId);

        if(room == null){
            view.showInfoMessage("Room not found!");
            return;
        }

        venue = interactor.getVenue(room.getVenueId());

        if(venue == null){
            view.showInfoMessage("Venue not found!");
            return;
        }

        if(room.getFloorId() > 0)
            floor = interactor.getFloor(room.getFloorId());
        String locationName = venue.getName();
        if(floor != null)
            locationName += " - " + floor.getName();
        locationName += " - " + room.getName();
        view.setVenueName(locationName);
        view.setLocation(venue.getAddress());
        List<String> images = venue.getImages();

        view.toggleImagesGallery(images.size() > 0);
        if (images.size() > 0) {
            view.setImages(images);
        }

        List<String> maps = venue.getMaps();
        if(floor != null && floor.getPictureUrl() != null){
            maps.add(floor.getPictureUrl());
        }
        if(room != null && room.getMapUrl() != null){
            maps.add(room.getMapUrl());
        }

        view.toggleMapNavigation(maps.size() > 0);
        view.toggleMapsGallery(maps.size() > 0);
        view.toggleMap(maps.size() == 0 && isVenueGeoLocated(venue));

        if (maps.size() > 0) {
            view.setMaps(maps);
        } else if (isVenueGeoLocated(venue)) {
            view.setMarker(venue);
        }
    }
}
