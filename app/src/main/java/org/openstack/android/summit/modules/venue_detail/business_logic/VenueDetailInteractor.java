package org.openstack.android.summit.modules.venue_detail.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.VenueDTO;
import org.openstack.android.summit.common.DTOs.VenueFloorDTO;
import org.openstack.android.summit.common.DTOs.VenueRoomDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.IVenueDataStore;
import org.openstack.android.summit.common.data_access.repositories.IVenueFloorDataStore;
import org.openstack.android.summit.common.data_access.repositories.IVenueRoomDataStore;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.entities.VenueFloor;
import org.openstack.android.summit.common.entities.VenueRoom;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public class VenueDetailInteractor extends BaseInteractor implements IVenueDetailInteractor {

    private IVenueDataStore venueDataStore;
    private IVenueRoomDataStore venueRoomDataStore;
    private IVenueFloorDataStore venueFloorDataStore;

    public VenueDetailInteractor
    (
        ISecurityManager securityManager,
        IVenueDataStore venueDataStore,
        IVenueRoomDataStore venueRoomDataStore,
        IVenueFloorDataStore venueFloorDataStore,
        IDTOAssembler dtoAssembler,
        ISummitDataStore summitDataStore,
        ISummitSelector summitSelector
    )
    {
        super(securityManager, dtoAssembler, summitSelector, summitDataStore);

        this.venueDataStore      = venueDataStore;
        this.venueRoomDataStore  = venueRoomDataStore;
        this.venueFloorDataStore = venueFloorDataStore;
    }

    @Override
    public VenueDTO getVenue(int venueId) {
        Venue venue = venueDataStore.getById(venueId);
        return venue != null ? dtoAssembler.createDTO(venue, VenueDTO.class):null;
    }

    @Override
    public VenueRoomDTO getRoom(int roomId) {
        VenueRoom room = venueRoomDataStore.getById(roomId);
        return (room != null) ? dtoAssembler.createDTO(room, VenueRoomDTO.class) : null;
    }

    @Override
    public VenueFloorDTO getFloor(int floorId) {
        VenueFloor floor = venueFloorDataStore.getById(floorId);
        return ( floor != null ) ? dtoAssembler.createDTO(floor, VenueFloorDTO.class) : null;
    }

    @Override
    public List<VenueRoomDTO> getVenueRooms(int venueId) {
        List<VenueRoom> venueRooms = venueRoomDataStore.getAll();
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
        Venue venue = venueDataStore.getById(venueId);
        return  createDTOList(venue.getFloors().sort("number"), VenueFloorDTO.class);
    }
}
