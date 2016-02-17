package org.openstack.android.summit.modules.venue_detail.user_interface;

import org.openstack.android.summit.common.DTOs.VenueListItemDTO;
import org.openstack.android.summit.common.DTOs.VenueRoomDTO;
import org.openstack.android.summit.common.user_interface.IBaseView;

import java.util.List;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public interface IVenueDetailView extends IBaseView {
    void setVenueName(String name);

    void setVenueRooms(List<VenueRoomDTO> rooms);

    void setLocation(String address);

    void setMarker(VenueListItemDTO venue);

    void setMaps(List<String> maps);

    void toggleMap(boolean visible);

    void toggleGallery(boolean visible);

    void toggleMapNavigation(boolean visible);
}
