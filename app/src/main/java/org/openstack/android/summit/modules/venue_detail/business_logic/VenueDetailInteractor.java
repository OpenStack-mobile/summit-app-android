package org.openstack.android.summit.modules.venue_detail.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.VenueDTO;
import org.openstack.android.summit.common.DTOs.VenueRoomDTO;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.entities.VenueRoom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public class VenueDetailInteractor extends BaseInteractor implements IVenueDetailInteractor {
    IGenericDataStore genericDataStore;

    public VenueDetailInteractor(IGenericDataStore genericDataStore, IDTOAssembler dtoAssembler) {
        super(dtoAssembler);
        this.genericDataStore = genericDataStore;
    }

    @Override
    public VenueDTO getVenue(int venueId) {
        Venue venue = genericDataStore.getByIdLocal(venueId, Venue.class);
        VenueDTO dto = dtoAssembler.createDTO(venue, VenueDTO.class);
        return dto;
    }

    @Override
    public List<VenueRoomDTO> getVenueRooms(int venueId) {
        List<VenueRoom> venueRooms = genericDataStore.getaAllLocal(VenueRoom.class);
        List<VenueRoom> filteredVenueRooms = new ArrayList<>();
        for (VenueRoom venueRoom: venueRooms) {
            if (venueRoom.getVenue().getId() == venueId) {
                filteredVenueRooms.add(venueRoom);
            }
        }
        List<VenueRoomDTO> dtos = createDTOList(filteredVenueRooms, VenueRoomDTO.class);
        return dtos;
    }
}
