package org.openstack.android.summit.modules.venue_detail.business_logic;

import org.openstack.android.summit.common.DTOs.VenueDTO;
import org.openstack.android.summit.common.DTOs.VenueRoomDTO;
import org.openstack.android.summit.common.business_logic.IBaseInteractor;

import java.util.List;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public interface IVenueDetailInteractor extends IBaseInteractor {
    VenueDTO getVenue(int venueId);

    List<VenueRoomDTO> getVenueRooms(int venueId);
}
