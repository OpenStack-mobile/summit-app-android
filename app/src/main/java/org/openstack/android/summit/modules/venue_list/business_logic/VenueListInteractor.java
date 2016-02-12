package org.openstack.android.summit.modules.venue_list.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.entities.Venue;

import java.util.List;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenueListInteractor extends BaseInteractor implements IVenueListInteractor {
    private IGenericDataStore genericDataStore;

    public VenueListInteractor(IGenericDataStore genericDataStore, IDTOAssembler dtoAssembler) {
        super(dtoAssembler);
        this.genericDataStore = genericDataStore;
    }

    @Override
    public List<NamedDTO> getVenues() {
        List<Venue> venues = genericDataStore.getaAllLocal(Venue.class);
        List<NamedDTO> dtos = createDTOList(venues, NamedDTO.class);
        return dtos;
    }
}
