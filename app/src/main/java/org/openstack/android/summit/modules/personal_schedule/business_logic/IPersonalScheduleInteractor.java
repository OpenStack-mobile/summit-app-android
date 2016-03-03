package org.openstack.android.summit.modules.personal_schedule.business_logic;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.business_logic.IScheduleInteractor;

import java.util.Date;
import java.util.List;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public interface IPersonalScheduleInteractor extends IScheduleInteractor {
    List<ScheduleItemDTO> getCurrentMemberScheduledEvents(Date startDate, Date endDate);

    List<DateTime> getCurrentMemberScheduleDatesWithoutEvents(DateTime startDate, DateTime endDate);
}
