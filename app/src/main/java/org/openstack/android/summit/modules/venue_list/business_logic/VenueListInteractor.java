package org.openstack.android.summit.modules.venue_list.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.VenueDTO;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.common.entities.Venue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenueListInteractor extends BaseInteractor implements IVenueListInteractor {
    private IGenericDataStore genericDataStore;

    public VenueListInteractor(IGenericDataStore genericDataStore, IDTOAssembler dtoAssembler, IDataUpdatePoller dataUpdatePoller) {
        super(dtoAssembler, dataUpdatePoller);
        this.genericDataStore = genericDataStore;
    }

    @Override
    public List<VenueDTO> getInternalVenues() {
        List<Venue> venues = genericDataStore.getAllLocal(Venue.class);
        List<Venue> internalVenues = new ArrayList<>();
        for (Venue venue: venues) {
            if (venue.getIsInternal()) {
                internalVenues.add(venue);
            }
        }

        List<VenueDTO> dtos = createDTOList(internalVenues, VenueDTO.class);
        return dtos;
    }

    @Override
    public List<VenueDTO> getExternalVenues() {
        List<Venue> venues = genericDataStore.getAllLocal(Venue.class);
        List<Venue> externalVenues = new ArrayList<>();
        for (Venue venue: venues) {
            if (!venue.getIsInternal()) {
                externalVenues.add(venue);
            }
        }

        List<VenueDTO> dtos = createDTOList(externalVenues, VenueDTO.class);
        return dtos;
    }

}
