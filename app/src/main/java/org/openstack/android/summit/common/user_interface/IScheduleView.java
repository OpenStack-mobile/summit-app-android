package org.openstack.android.summit.common.user_interface;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;

import java.util.List;

/**
 * Created by Claudio Redi on 12/29/2015.
 */
public interface IScheduleView extends IBaseView {

    void setStartAndEndDateWithParts(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay);

    void setStartAndEndDateWithDisabledDates(DateTime startDate, DateTime endDate, List<DateTime> disabledDates);

    void setDisabledDates(List<DateTime> disabledDates);

    void setEvents(List<ScheduleItemDTO> events);

    DateTime getSelectedDate();

    int getSelectedDay();

    void setSelectedDate(int day, boolean notifyListeners);

    void setSelectedDate(int day);

    void reloadSchedule();

    void showEmptyMessage(boolean show);

    void setNowButtonVisibility(int visibility);

    void setListPosition(int newPosition);

}
