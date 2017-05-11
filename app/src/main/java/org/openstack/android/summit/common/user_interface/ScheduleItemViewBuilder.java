package org.openstack.android.summit.common.user_interface;

import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;

/**
 * Created by Claudio Redi on 1/14/2016.
 */
public class ScheduleItemViewBuilder implements IScheduleItemViewBuilder {

    @Override
    public void build(IScheduleItemView scheduleItemView, ScheduleItemDTO scheduleItemDTO, boolean isMemberLoggedIn, boolean isMemberLoggedInAndAttendee, boolean isEventScheduledByLoggedMember, boolean isEventFavoriteByLoggedMember, boolean useFullDate, boolean showVenues, String rsvpLink, boolean externalRSVP, boolean allowRate, boolean toBeRecorded) {
         build(scheduleItemView, scheduleItemDTO, isMemberLoggedIn, isMemberLoggedInAndAttendee, isEventScheduledByLoggedMember, isEventFavoriteByLoggedMember,  useFullDate, showVenues, rsvpLink, externalRSVP, allowRate, toBeRecorded, true, true);
    }

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
        boolean showVenues,
        String rsvpLink,
        boolean externalRSVP,
        boolean allowRate,
        boolean toBeRecorded,
        boolean showMyScheduleOptions,
        boolean showMyFavoritesOptions
    ) {
        scheduleItemView.setName(scheduleItemDTO.getName());
        scheduleItemView.setTime(useFullDate ? scheduleItemDTO.getDateTime() : scheduleItemDTO.getTime());
        scheduleItemView.setSponsors(scheduleItemDTO.getSponsors());
        scheduleItemView.setEventType(scheduleItemDTO.getEventType().toUpperCase());
        scheduleItemView.setTrack(scheduleItemDTO.getTrack());

        scheduleItemView.setScheduled(showMyScheduleOptions && isEventScheduledByLoggedMember);
        scheduleItemView.setFavorite(showMyFavoritesOptions && isEventFavoriteByLoggedMember);
        scheduleItemView.shouldShowFavoritesOption(showMyFavoritesOptions);
        scheduleItemView.shouldShowGoingToOption(showMyScheduleOptions && (rsvpLink == null || rsvpLink.isEmpty()));
        scheduleItemView.shouldShowRSVPToOption(showMyScheduleOptions  && rsvpLink != null && !rsvpLink.isEmpty());
        scheduleItemView.shouldShowUnRSVPToOption(showMyScheduleOptions  && rsvpLink != null && !rsvpLink.isEmpty() && isEventScheduledByLoggedMember);
        scheduleItemView.shouldShowAllowRate(allowRate);
        scheduleItemView.setToBeRecorded(toBeRecorded);
        scheduleItemView.setContextualMenu();

        String color = scheduleItemDTO.getColor() != null && scheduleItemDTO.getColor() != ""
                ? scheduleItemDTO.getColor()
                : "";

        scheduleItemView.setColor(color);

        scheduleItemView.showLocation(showVenues);
        if(showVenues)
            scheduleItemView.setLocation(scheduleItemDTO.getLocation());

        scheduleItemView.setRSVPLink(rsvpLink);
        scheduleItemView.setExternalRSVP(externalRSVP);
    }
}
