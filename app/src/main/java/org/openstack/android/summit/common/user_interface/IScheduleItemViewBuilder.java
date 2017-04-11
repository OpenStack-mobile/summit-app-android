package org.openstack.android.summit.common.user_interface;

import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;

/**
 * Created by Claudio Redi on 1/14/2016.
 */
public interface IScheduleItemViewBuilder {

    void build
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
        boolean allowRate
    );

    void build
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
        boolean showMyScheduleOptions,
        boolean showMyFavoritesOptions
    );

}
