package org.openstack.android.summit.modules.personal_schedule.business_logic;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.ScheduleInteractor;
import org.openstack.android.summit.common.data_access.repositories.IMemberDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Sort;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public class PersonalScheduleInteractor extends ScheduleInteractor implements IPersonalScheduleInteractor {

    public PersonalScheduleInteractor(IMemberDataStore memberDataStore, ISummitEventDataStore summitEventDataStore, ISummitDataStore summitDataStore, IDTOAssembler dtoAssembler, ISecurityManager securityManager, IPushNotificationsManager pushNotificationsManager, ISession session, ISummitSelector summitSelector, IReachability reachability) {
        super(summitEventDataStore, summitDataStore, memberDataStore, dtoAssembler, securityManager, pushNotificationsManager, session, summitSelector, reachability);
    }

    @Override
    public List<ScheduleItemDTO> getCurrentMemberScheduledEvents(Date startDate, Date endDate) {
        Member member              = securityManager.getCurrentMember();

        if(member == null) return new ArrayList<>();

        List<SummitEvent> scheduleEvents =  member.getScheduledEvents()
            .where()
            .greaterThanOrEqualTo("start", startDate)
            .lessThanOrEqualTo("end", endDate)
            .sort(new String[]{"start", "end", "name"}, new Sort[]{Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING})
            .findAll();

        return postProcessScheduleEventList(createDTOList(scheduleEvents, ScheduleItemDTO.class));
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
