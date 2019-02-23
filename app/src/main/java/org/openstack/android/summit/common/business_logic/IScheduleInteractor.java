package org.openstack.android.summit.common.business_logic;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.filters.FilterConditions;

import java.util.List;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public interface IScheduleInteractor
        extends IScheduleableInteractor {

    List<ScheduleItemDTO> getScheduleEvents(FilterConditions conditions);

    List<DateTime> getDatesWithoutEvents(FilterConditions conditions);

    boolean eventExist(int id);

    ScheduleItemDTO getEvent(int eventId);
}
