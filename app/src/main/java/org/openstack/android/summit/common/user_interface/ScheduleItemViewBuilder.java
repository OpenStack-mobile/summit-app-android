package org.openstack.android.summit.common.user_interface;

import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;

/**
 * Created by Claudio Redi on 1/14/2016.
 */
public class ScheduleItemViewBuilder implements IScheduleItemViewBuilder {
    @Override
    public void build
    (
        IScheduleItemView scheduleItemView,
        ScheduleItemDTO scheduleItemDTO,
        boolean isMemberLoggedIn,
        boolean isMemberLoggedInAndAttendee,
        boolean isEventScheduledByLoggedMember,
        boolean isEventFavoriteByLoggedMember,
        boolean useFullDate,
        boolean showVenues
    ) {
        scheduleItemView.setName(scheduleItemDTO.getName());
        scheduleItemView.setTime(useFullDate ? scheduleItemDTO.getDateTime() : scheduleItemDTO.getTime());
        scheduleItemView.setSponsors(scheduleItemDTO.getSponsors());
        scheduleItemView.setEventType(scheduleItemDTO.getEventType().toUpperCase());
        scheduleItemView.setTrack(scheduleItemDTO.getTrack());

        if (isMemberLoggedIn) {
            scheduleItemView.setScheduled(isEventScheduledByLoggedMember);
            scheduleItemView.setFavorite(isEventFavoriteByLoggedMember);
            scheduleItemView.shouldShowFavoritesOption(true);
            scheduleItemView.shouldShowGoingToOption(isMemberLoggedInAndAttendee);
        }
        else{
            scheduleItemView.setScheduled(false);
            scheduleItemView.setFavorite(false);
            scheduleItemView.shouldShowFavoritesOption(false);
            scheduleItemView.shouldShowGoingToOption(false);
        }
        scheduleItemView.setContextualMenu();
        String color = scheduleItemDTO.getColor() != null && scheduleItemDTO.getColor() != ""
                ? scheduleItemDTO.getColor()
                : "";

        scheduleItemView.setColor(color);

        scheduleItemView.showLocation(showVenues);
        if(showVenues)
            scheduleItemView.setLocation(scheduleItemDTO.getLocation());
    }
}
