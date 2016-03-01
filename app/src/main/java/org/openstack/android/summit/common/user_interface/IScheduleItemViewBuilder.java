package org.openstack.android.summit.common.user_interface;

import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;

/**
 * Created by Claudio Redi on 1/14/2016.
 */
public interface IScheduleItemViewBuilder {
    void build(IScheduleItemView scheduleItemView, ScheduleItemDTO scheduleItemDTO, Boolean isMemberLoggedIn, Boolean isEventScheduledByLoggedMember, boolean useFullDate, boolean showVenues);
}
