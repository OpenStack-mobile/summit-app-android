package org.openstack.android.summit.modules.events.business_logic;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.network.IReachability;

/**
 * Created by Claudio Redi on 2/10/2016.
 */
public class EventsInteractor  extends BaseInteractor implements IEventsInteractor {

    protected ISummitDataStore summitDataStore;
    protected IReachability reachability;
    protected ISummitSelector summitSelector;

    public EventsInteractor
    (
        ISummitDataStore summitDataStore,
        IReachability reachability,
        IDTOAssembler dtoAssembler,
        ISummitSelector summitSelector
    )
    {
        super(dtoAssembler, summitSelector, summitDataStore);
        this.summitDataStore = summitDataStore;
        this.reachability    = reachability;
        this.summitSelector  = summitSelector;
    }

    @Override
    public boolean isNetworkingAvailable() {
        return reachability.isNetworkingAvailable(OpenStackSummitApplication.context);
    }
}
