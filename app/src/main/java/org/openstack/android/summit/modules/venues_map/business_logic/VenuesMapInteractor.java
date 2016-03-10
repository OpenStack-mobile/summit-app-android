package org.openstack.android.summit.modules.venues_map.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.VenueDTO;
import org.openstack.android.summit.common.DTOs.VenueListItemDTO;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.common.entities.Venue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenuesMapInteractor extends BaseInteractor implements IVenuesMapInteractor {
    private IGenericDataStore genericDataStore;

    public VenuesMapInteractor(IGenericDataStore genericDataStore, IDTOAssembler dtoAssembler, IDataUpdatePoller dataUpdatePoller) {
        super(dtoAssembler, dataUpdatePoller);
        this.genericDataStore = genericDataStore;
    }

    @Override
    public List<VenueListItemDTO> getInternalVenuesWithCoordinates() {
        List<Venue> venues = genericDataStore.getAllLocal(Venue.class);
        List<Venue> venuesWithCoordinates = new ArrayList<>();
        for (Venue venue: venues) {
            if (venue.getIsInternal() && venue.getLat() != null && !venue.getLat().isEmpty() && venue.getLng() != null && !venue.getLng().isEmpty()) {
                venuesWithCoordinates.add(venue);
            }
        }
        List<VenueListItemDTO> dtos = createDTOList(venuesWithCoordinates, VenueListItemDTO.class);
        return dtos;
    }
}
