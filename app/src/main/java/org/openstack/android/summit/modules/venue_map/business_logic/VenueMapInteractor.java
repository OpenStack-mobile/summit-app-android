package org.openstack.android.summit.modules.venue_map.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.VenueDTO;
import org.openstack.android.summit.common.DTOs.VenueListItemDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.entities.Venue;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenueMapInteractor extends BaseInteractor implements IVenueMapInteractor {
    private IGenericDataStore genericDataStore;

    public VenueMapInteractor(IGenericDataStore genericDataStore, IDTOAssembler dtoAssembler, ISummitDataStore summitDataStore, ISummitSelector summitSelector) {
        super(dtoAssembler, summitSelector, summitDataStore);
        this.genericDataStore = genericDataStore;
    }

    @Override
    public VenueListItemDTO getVenue(int venueId) {
        Venue venue = genericDataStore.getByIdLocal(venueId, Venue.class);
        if(venue == null) return null;
        return dtoAssembler.createDTO(venue, VenueDTO.class);
    }
}
