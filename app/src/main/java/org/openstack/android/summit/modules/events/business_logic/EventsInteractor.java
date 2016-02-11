package org.openstack.android.summit.modules.events.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.business_logic.IBaseInteractor;

/**
 * Created by Claudio Redi on 2/10/2016.
 */
public class EventsInteractor  extends BaseInteractor implements IEventsInteractor {
    public EventsInteractor(IDTOAssembler dtoAssembler) {
        super(dtoAssembler);
    }
}
