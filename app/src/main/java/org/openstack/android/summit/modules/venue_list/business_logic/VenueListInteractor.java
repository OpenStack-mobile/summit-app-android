package org.openstack.android.summit.modules.venue_list.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.VenueDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.IVenueDataStore;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.List;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenueListInteractor extends BaseInteractor implements IVenueListInteractor {

    private IVenueDataStore venueDataStore;

    public VenueListInteractor
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
    public List<VenueDTO> getInternalVenues() {
        return createDTOList(venueDataStore.getInternalsBySummit(summitSelector.getCurrentSummitId()), VenueDTO.class);
    }

    @Override
    public List<VenueDTO> getExternalVenues() {
        return createDTOList(venueDataStore.getExternalBySummit(summitSelector.getCurrentSummitId()), VenueDTO.class);
    }

}
