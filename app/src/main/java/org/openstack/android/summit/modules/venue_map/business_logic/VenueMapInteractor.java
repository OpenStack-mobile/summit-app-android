package org.openstack.android.summit.modules.venue_map.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.VenueDTO;
import org.openstack.android.summit.common.DTOs.VenueListItemDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.IVenueDataStore;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenueMapInteractor extends BaseInteractor implements IVenueMapInteractor {

    private IVenueDataStore venueDataStore;

    public VenueMapInteractor(ISecurityManager securityManager, IVenueDataStore venueDataStore, IDTOAssembler dtoAssembler, ISummitDataStore summitDataStore, ISummitSelector summitSelector, IReachability reachability) {
        super(securityManager, dtoAssembler, summitSelector, summitDataStore, reachability);
        this.venueDataStore = venueDataStore;
    }

    @Override
    public VenueListItemDTO getVenue(int venueId) {
        Venue venue = venueDataStore.getById(venueId);
        if(venue == null) return null;
        return dtoAssembler.createDTO(venue, VenueDTO.class);
    }
}
