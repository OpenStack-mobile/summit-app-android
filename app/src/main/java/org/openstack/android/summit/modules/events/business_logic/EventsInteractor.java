package org.openstack.android.summit.modules.events.business_logic;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.network.IReachability;

/**
 * Created by Claudio Redi on 2/10/2016.
 */
public class EventsInteractor  extends BaseInteractor implements IEventsInteractor {
    private ISummitDataStore summitDataStore;
    private IReachability reachability;

    public EventsInteractor(ISummitDataStore summitDataStore, IReachability reachability, IDTOAssembler dtoAssembler) {
        super(dtoAssembler);
        this.summitDataStore = summitDataStore;
        this.reachability    = reachability;
    }

    @Override
    public boolean isDataLoaded() {
        return summitDataStore.getActive() != null;
    }

    @Override
    public SummitDTO getLocalActiveSummit(){
        Summit currentSummit = summitDataStore.getActive();
        if(currentSummit == null) return null;
        return dtoAssembler.createDTO(currentSummit, SummitDTO.class);
    }

    @Override
    public boolean isNetworkingAvailable() {
        return reachability.isNetworkingAvailable(OpenStackSummitApplication.context);
    }
}
