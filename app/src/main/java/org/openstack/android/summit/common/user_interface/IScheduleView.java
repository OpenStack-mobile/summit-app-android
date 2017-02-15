package org.openstack.android.summit.common.user_interface;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;

import java.util.Date;
import java.util.List;

/**
 * Created by Claudio Redi on 12/29/2015.
 */
public interface IScheduleView extends IBaseView {

    void setStartAndEndDateWithParts(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay);

    void setStartAndEndDateWithDisabledDates(DateTime startDate, DateTime endDate, List<DateTime> disabledDates);

    void setDisabledDates(List<DateTime> disabledDates);

    List<ScheduleItemDTO> getEvents();

    void setEvents(List<ScheduleItemDTO> events);

    DateTime getSelectedDate();

    void setSelectedDate(int day);

    void reloadSchedule();

    void showEmptyMessage(boolean show);

    void setNowButtonVisibility(int visibility);

    void setNowButtonState(boolean isChecked);

    void setNowButtonListener();

    void clearNowButtonListener();

    boolean getNowButtonState();

}
