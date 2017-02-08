package org.openstack.android.summit.modules.venues_map.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.VenueListItemDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.IVenueDataStore;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenuesMapInteractor extends BaseInteractor implements IVenuesMapInteractor {

    private IVenueDataStore venueDataStore;

    public VenuesMapInteractor
    (
        ISecurityManager securityManager,
        IVenueDataStore venueDataStore,
        IDTOAssembler dtoAssembler,
        ISummitDataStore summitDataStore,
        ISummitSelector summitSelector
    )
    {
        super(securityManager, dtoAssembler, summitSelector, summitDataStore);
        this.venueDataStore = venueDataStore;
    }

    @Override
    public List<VenueListItemDTO> getInternalVenuesWithCoordinates() {
        List<Venue> venues                = venueDataStore.getInternalsBySummit(summitSelector.getCurrentSummitId());
        List<Venue> venuesWithCoordinates = new ArrayList<>();
        for (Venue venue: venues) {
            if (venue.getLat() != null && !venue.getLat().isEmpty() && venue.getLng() != null && !venue.getLng().isEmpty()) {
                venuesWithCoordinates.add(venue);
            }
        }
        return createDTOList(venuesWithCoordinates, VenueListItemDTO.class);
    }
}
