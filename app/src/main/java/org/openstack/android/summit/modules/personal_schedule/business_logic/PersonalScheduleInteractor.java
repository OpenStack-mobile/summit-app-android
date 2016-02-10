package org.openstack.android.summit.modules.personal_schedule.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.business_logic.ScheduleInteractor;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.List;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public class PersonalScheduleInteractor extends ScheduleInteractor implements IPersonalScheduleInteractor {

    public PersonalScheduleInteractor(ISummitEventDataStore summitEventDataStore, ISummitDataStore summitDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, IDTOAssembler dtoAssembler, ISecurityManager securityManager, IDataUpdatePoller dataUpdatePoller) {
        super(summitEventDataStore, summitDataStore, summitAttendeeDataStore, dtoAssembler, securityManager, dataUpdatePoller);
    }

    @Override
    public List<ScheduleItemDTO> getCurrentMemberScheduledEvents() {
        Member member = securityManager.getCurrentMember();
        List<ScheduleItemDTO> dtos = createDTOList(member.getAttendeeRole().getScheduledEvents(), ScheduleItemDTO.class);
        return dtos;
    }
}
