package org.openstack.android.summit.modules.venues.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenuesInteractor extends BaseInteractor implements IVenuesInteractor {

    public VenuesInteractor
    (
        ISecurityManager securityManager,
        IDTOAssembler dtoAssembler,
        ISummitSelector summitSelector,
        ISummitDataStore summitDataStore,
        IReachability reachability
    )
    {
        super(securityManager, dtoAssembler, summitSelector, summitDataStore, reachability);
    }
}
