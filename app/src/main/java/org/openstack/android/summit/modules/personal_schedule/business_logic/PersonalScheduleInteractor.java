package org.openstack.android.summit.modules.personal_schedule.business_logic;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.IMemberDataStore;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.business_logic.ScheduleInteractor;
import org.openstack.android.summit.common.data_access.repositories.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.security.ISecurityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import io.realm.Sort;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public class PersonalScheduleInteractor extends ScheduleInteractor implements IPersonalScheduleInteractor {

    public PersonalScheduleInteractor(IMemberDataStore memberDataStore, ISummitEventDataStore summitEventDataStore, ISummitDataStore summitDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, IDTOAssembler dtoAssembler, ISecurityManager securityManager, IPushNotificationsManager pushNotificationsManager, ISession session, ISummitSelector summitSelector) {
        super(summitEventDataStore, summitDataStore, summitAttendeeDataStore, memberDataStore, dtoAssembler, securityManager, pushNotificationsManager, session, summitSelector);
    }

    @Override
    public List<ScheduleItemDTO> getCurrentMemberScheduledEvents(Date startDate, Date endDate) {
        Member member = securityManager.getCurrentMember();
        List<ScheduleItemDTO> dtos;
        if (member != null) {

            List<SummitEvent> scheduleEvents = member.getAttendeeRole().getScheduledEvents()
                    .where()
                    .greaterThanOrEqualTo("start", startDate)
                    .lessThanOrEqualTo("end", endDate)
                    .findAllSorted(new String[]{"start", "end", "name"}, new Sort[]{Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING});

            List<SummitEvent> favoriteEvents = member.getFavoriteEvents()
                    .where()
                    .greaterThanOrEqualTo("start", startDate)
                    .lessThanOrEqualTo("end", endDate)
                    .findAllSorted(new String[]{"start", "end", "name"}, new Sort[]{Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING});

            List<SummitEvent> finalList = new ArrayList<>();
            finalList.addAll(scheduleEvents);
            for (SummitEvent f : favoriteEvents){
                if (!finalList.contains(f))
                    finalList.add(f);
            }

            Collections.sort(finalList, (s1, s2) -> {
                int startCond = s1.getStart().compareTo(s2.getStart());
                if (startCond != 0) {
                    return startCond;
                } else {
                    int endCond   = s1.getEnd().compareTo(s2.getEnd());
                    if(endCond != 0)
                       return endCond;
                }
                return s1.getName().compareTo(s2.getName());
            });

            dtos = createDTOList(finalList, ScheduleItemDTO.class);
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
