package org.openstack.android.summit.common.user_interface;

import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;

import java.util.Date;
import java.util.List;

/**
 * Created by Claudio Redi on 12/29/2015.
 */
public interface IScheduleFragment {
    void setStartAndEndDateWithParts(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay);

    List<ScheduleItemDTO> getEvents();

    void setEvents(List<ScheduleItemDTO> events);

    Date getSelectedDate();

    void setSelectedDate(Date date);

    void reloadSchedule();
}
