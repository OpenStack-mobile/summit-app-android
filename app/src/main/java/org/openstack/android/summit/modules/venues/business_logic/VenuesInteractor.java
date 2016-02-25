package org.openstack.android.summit.modules.venues.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenuesInteractor extends BaseInteractor implements IVenuesInteractor {
    public VenuesInteractor(IDTOAssembler dtoAssembler, IDataUpdatePoller dataUpdatePoller) {
        super(dtoAssembler, dataUpdatePoller);
    }
}
