package org.openstack.android.summit.modules.venue_detail.user_interface;

import android.os.Bundle;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.VenueDTO;
import org.openstack.android.summit.common.DTOs.VenueFloorDTO;
import org.openstack.android.summit.common.DTOs.VenueRoomDTO;
import org.openstack.android.summit.common.user_interface.AlertsBuilder;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.common.user_interface.ISimpleListItemView;
import org.openstack.android.summit.modules.venue_detail.IVenueDetailWireframe;
import org.openstack.android.summit.modules.venue_detail.business_logic.IVenueDetailInteractor;

import java.util.List;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public class VenueDetailPresenter extends BasePresenter<IVenueDetailView, IVenueDetailInteractor, IVenueDetailWireframe> implements IVenueDetailPresenter {

    protected VenueDTO venue;
    protected List<VenueRoomDTO> venueRooms;
    protected Integer venueId = null;

    public VenueDetailPresenter(IVenueDetailInteractor interactor, IVenueDetailWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            venueId = (savedInstanceState != null) ?
                    savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_VENUE, 0) :
                    wireframe.getParameter(Constants.NAVIGATION_PARAMETER_VENUE, Integer.class);
        } catch (Exception ex) {
            Log.e(Constants.LOG_TAG, ex.getMessage());
            Crashlytics.logException(ex);
        }
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);

        venue = interactor.getVenue(venueId != null ? venueId : 0);

        if (venue == null) {
            AlertsBuilder.buildAlert(view.getFragmentActivity(), R.string.generic_info_title, R.string.venue_not_exists).show();
            return;
        }

        view.setVenueName(venue.getName());
        view.setLocation(venue.getAddress());

        view.toggleImagesGallery(venue.getImages().size() > 0);
        if (venue.getImages().size() > 0) {
            view.setImages(venue.getImages());
        }

        List<String> maps = venue.getMaps();
        for (VenueFloorDTO floor : interactor.getVenueFloors(venueId)) {
            if (floor.getPictureUrl() != null) maps.add(floor.getPictureUrl());
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

    protected boolean isVenueGeoLocated(VenueDTO venue) {
        return venue.getLat() != null && !venue.getLat().isEmpty() && venue.getLng() != null && !venue.getLng().isEmpty();
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (venueId != null) {
            outState.putInt(Constants.NAVIGATION_PARAMETER_VENUE, venueId);
        }
    }
}
