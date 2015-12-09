package org.openstack.android.summit.common.DTOs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class VenueDTO extends VenueListItemDTO {
    public String address;
    public List<VenueRoomDTO> rooms = new ArrayList<VenueRoomDTO>();
}
