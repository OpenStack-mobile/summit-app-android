package org.openstack.android.summit.common.business_logic;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.IMemberDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.filters.DateRangeCondition;
import org.openstack.android.summit.common.filters.FilterConditions;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class ScheduleInteractor extends ScheduleableInteractor implements IScheduleInteractor {

    private ISession session;
    private final String PUSH_NOTIFICATIONS_SUBSCRIBED_KEY = "PUSH_NOTIFICATIONS_SUBSCRIBED_KEY";

    @Inject
    public ScheduleInteractor
    (
            ISummitEventDataStore summitEventDataStore,
            ISummitDataStore summitDataStore,
            IMemberDataStore memberDataStore,
            IDTOAssembler dtoAssembler,
            ISecurityManager securityManager,
            IPushNotificationsManager pushNotificationsManager,
            ISession session,
            ISummitSelector summitSelector,
            IReachability reachability
    ) {
        super
        (
            summitEventDataStore,
            summitDataStore,
            memberDataStore,
            dtoAssembler,
            securityManager,
            pushNotificationsManager,
            summitSelector,
            reachability
        );
        this.summitDataStore = summitDataStore;
        this.session = session;
    }

    @Override
    public List<ScheduleItemDTO> getScheduleEvents
    (
            FilterConditions conditions
    )
    {
        return postProcessScheduleEventList(createDTOList(
                summitEventDataStore.getByFilter
                        (
                                conditions
                        ),
                ScheduleItemDTO.class
        ));
    }

    @Override
    public List<DateTime> getDatesWithoutEvents
    (
            FilterConditions conditions
    ) {
        ArrayList<DateTime> inactiveDates = new ArrayList<>();
        List<SummitEvent> events;
        DateTime startDate = conditions.getStartDate();
        DateTime endDate   = conditions.getEndDate();
        while (startDate.isBefore(endDate)) {
            events = summitEventDataStore.getByFilter
                    (
                        new DateRangeCondition(
                            startDate.withTime(0, 0, 0, 0),
                            startDate.withTime(23, 59, 59, 999)
                        ),
                        conditions
                    );
            if (events.size() == 0) {
                inactiveDates.add(startDate);
            }
            startDate = startDate.plusDays(1);
        }

        return inactiveDates;
    }

    @Override
    public boolean eventExist(int id) {
        SummitEvent summitEvent = summitEventDataStore.getById(id);
        return summitEvent != null;
    }

    @Override
    public ScheduleItemDTO getEvent(int eventId) {
        SummitEvent event= summitEventDataStore.getById(eventId);
        if(event != null){
            return postProcessScheduleEvent(securityManager.getCurrentMemberId(), createDTO(event, ScheduleItemDTO.class));
        }
        return null;
    }

}
