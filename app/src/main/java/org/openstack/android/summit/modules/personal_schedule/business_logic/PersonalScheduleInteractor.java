package org.openstack.android.summit.modules.personal_schedule.business_logic;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.IPushNotificationsManager;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.business_logic.ScheduleInteractor;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public class PersonalScheduleInteractor extends ScheduleInteractor implements IPersonalScheduleInteractor {

    public PersonalScheduleInteractor(ISummitEventDataStore summitEventDataStore, ISummitDataStore summitDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, IDTOAssembler dtoAssembler, ISecurityManager securityManager, IPushNotificationsManager pushNotificationsManager, ISession session, IDataUpdatePoller dataUpdatePoller) {
        super(summitEventDataStore, summitDataStore, summitAttendeeDataStore, dtoAssembler, securityManager, pushNotificationsManager, session, dataUpdatePoller);
    }

    @Override
    public List<ScheduleItemDTO> getCurrentMemberScheduledEvents(Date startDate, Date endDate) {
        Member member = securityManager.getCurrentMember();
        List<ScheduleItemDTO> dtos;
        if (member != null) {
            List<SummitEvent> events = member.getAttendeeRole().getScheduledEvents()
                    .where()
                    .greaterThanOrEqualTo("start", startDate)
                    .lessThanOrEqualTo("end", endDate)
                    .findAll();

            dtos = createDTOList(events, ScheduleItemDTO.class);
        }
        else {
            dtos = new ArrayList<>();
        }
        return dtos;
    }

    @Override
    public List<DateTime> getCurrentMemberScheduleDatesWithoutEvents(DateTime startDate, DateTime endDate) {
        ArrayList<DateTime> inactiveDates = new ArrayList<>();
        List<ScheduleItemDTO> events;

        while(startDate.isBefore(endDate)) {
            events = getCurrentMemberScheduledEvents(
                    startDate.withTime(0, 0, 0, 0).toDate(),
                    startDate.withTime(23, 59, 59, 999).toDate()
            );
            if (events.size() == 0) {
                inactiveDates.add(startDate);
            }
            startDate = startDate.plusDays(1);
        }

        return inactiveDates;
    }
}
