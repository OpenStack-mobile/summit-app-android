package org.openstack.android.summit.modules.event_detail.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.EventDetailDTO;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.business_logic.ScheduleableInteractor;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.security.ISecurityManager;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/21/2016.
 */
public class EventDetailInteractor extends ScheduleableInteractor implements IEventDetailInteractor {

    @Inject
    public EventDetailInteractor(ISummitEventDataStore summitEventDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, IDTOAssembler dtoAssembler, ISecurityManager securityManager) {
        super(summitEventDataStore, summitAttendeeDataStore, dtoAssembler, securityManager);
    }

    @Override
    public EventDetailDTO getEventDetail(int eventId) {
        SummitEvent summitEvent = summitEventDataStore.getByIdLocal(eventId);
        EventDetailDTO dto = dtoAssembler.createDTO(summitEvent, EventDetailDTO.class);
        return dto;
    }
}
