package org.openstack.android.summit.modules.venue_detail.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.VenueDTO;
import org.openstack.android.summit.common.DTOs.VenueFloorDTO;
import org.openstack.android.summit.common.DTOs.VenueRoomDTO;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.entities.VenueFloor;
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
        return venue != null ? dtoAssembler.createDTO(venue, VenueDTO.class):null;
    }

    @Override
    public VenueRoomDTO getRoom(int roomId) {
        VenueRoom room = genericDataStore.getByIdLocal(roomId, VenueRoom.class);
        return (room != null) ? dtoAssembler.createDTO(room, VenueRoomDTO.class) : null;
    }

    @Override
    public VenueFloorDTO getFloor(int floorId) {
        VenueFloor floor = genericDataStore.getByIdLocal(floorId, VenueFloor.class);
        return ( floor != null ) ? dtoAssembler.createDTO(floor, VenueFloorDTO.class) : null;
    }

    @Override
    public List<VenueRoomDTO> getVenueRooms(int venueId) {
        List<VenueRoom> venueRooms = genericDataStore.getAllLocal(VenueRoom.class);
        List<VenueRoom> filteredVenueRooms = new ArrayList<>();
        for (VenueRoom venueRoom: venueRooms) {
            if (venueRoom.getVenue().getId() == venueId) {
                filteredVenueRooms.add(venueRoom);
            }
        }
        return createDTOList(filteredVenueRooms, VenueRoomDTO.class);
    }

    @Override
    public List<VenueFloorDTO> getVenueFloors(int venueId) {
        Venue venue = genericDataStore.getByIdLocal(venueId, Venue.class);
        return  createDTOList(venue.getFloors().sort("number"), VenueFloorDTO.class);
    }
}
