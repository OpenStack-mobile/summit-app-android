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
        boolean isEventScheduledByLoggedMember,
        boolean isEventFavoriteByLoggedMember,
        boolean useFullDate,
        boolean showVenues,
        String rsvpLink,
        boolean externalRSVP,
        boolean allowRate,
        boolean toBeRecorded
    );

    void build
    (
        IScheduleItemView scheduleItemView,
        ScheduleItemDTO scheduleItemDTO,
        boolean isMemberLoggedIn,
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
    );

}
