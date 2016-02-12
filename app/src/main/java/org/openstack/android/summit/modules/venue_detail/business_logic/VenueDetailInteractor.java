package org.openstack.android.summit.modules.venue_detail.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.business_logic.BaseInteractor;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public class VenueDetailInteractor extends BaseInteractor implements IVenueDetailInteractor {
    public VenueDetailInteractor(IDTOAssembler dtoAssembler) {
        super(dtoAssembler);
    }
}
