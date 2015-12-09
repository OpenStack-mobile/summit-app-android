package org.openstack.android.summit.modules.events.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.business_logic.ScheduleInteractor;
import org.openstack.android.summit.common.data_access.ISummitDataStore;

/**
 * Created by claudio on 10/30/2015.
 */
public class EventsInteractor extends ScheduleInteractor {

    public EventsInteractor(ISummitDataStore summitDataStore, IDTOAssembler dtoAssembler) {
        super(summitDataStore, dtoAssembler);
    }
}
