package org.openstack.android.summit.modules.personal_schedule.business_logic;

import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.business_logic.IScheduleInteractor;

import java.util.List;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public interface IPersonalScheduleInteractor extends IScheduleInteractor {
    List<ScheduleItemDTO> getCurrentMemberScheduledEvents();
}
