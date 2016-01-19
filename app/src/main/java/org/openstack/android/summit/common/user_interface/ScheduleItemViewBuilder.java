package org.openstack.android.summit.common.user_interface;

import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;

/**
 * Created by Claudio Redi on 1/14/2016.
 */
public class ScheduleItemViewBuilder implements IScheduleItemViewBuilder {
    @Override
    public void build(IScheduleItemView scheduleItemView, ScheduleItemDTO scheduleItemDTO, Boolean isMemberLoggedIn, Boolean isEventScheduledByLoggedMember) {
        scheduleItemView.setName(scheduleItemDTO.getName());
        scheduleItemView.setTime(scheduleItemDTO.getTime());
        scheduleItemView.setSponsors(scheduleItemDTO.getSponsors());
        scheduleItemView.setEventType(scheduleItemDTO.getEventType().toUpperCase());
        scheduleItemView.setTrack(scheduleItemDTO.getTrack());
        scheduleItemView.setIsScheduledStatusVisible(isMemberLoggedIn);
        if (isMemberLoggedIn) {
            scheduleItemView.setScheduled(isEventScheduledByLoggedMember);
        }
        String summitTypeColor = scheduleItemDTO.getSummitTypeColor() != "" ? scheduleItemDTO.getSummitTypeColor() : "#8A8A8A";
        scheduleItemView.setSummitTypeColor(summitTypeColor);
    }
}
