package org.openstack.android.summit.common.filters;

import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.modules.general_schedule_filter.user_interface.FilterSectionType;
import java.util.List;

final public class FilterConditionsBuilder {
    public static FilterConditions build(DateRangeCondition range, IScheduleFilter scheduleFilter){

        List<Integer> filtersOnEventTypes  = (List<Integer>) (List<?>) scheduleFilter.getSelections().get(FilterSectionType.EventType);
        List<Integer> filtersOnTrackGroups = (List<Integer>) (List<?>) scheduleFilter.getSelections().get(FilterSectionType.TrackGroup);
        List<Integer> filtersOnSummitTypes = (List<Integer>) (List<?>) scheduleFilter.getSelections().get(FilterSectionType.SummitType);
        List<String> filtersOnLevels       = (List<String>) (List<?>) scheduleFilter.getSelections().get(FilterSectionType.Level);
        List<String> filtersOnTags         = (List<String>) (List<?>) scheduleFilter.getSelections().get(FilterSectionType.Tag);
        List<Integer> filtersOnVenues      = (List<Integer>) (List<?>) scheduleFilter.getSelections().get(FilterSectionType.Venues);
        List<Integer> filtersOnRooms       = (List<Integer>) (List<?>) scheduleFilter.getSelections().get(FilterSectionType.Rooms);
        List<Boolean> filtersOnVideoTalks  = (List<Boolean>)(List<?>)scheduleFilter.getSelections().get(FilterSectionType.ShowVideoTalks);
        Boolean showVideoTalks             = (filtersOnVideoTalks != null && !filtersOnVideoTalks.isEmpty()) ? filtersOnVideoTalks.get(0) : false;
        List<Integer> filtersOnTracks      = (List<Integer>) (List<?>) scheduleFilter.getSelections().get(FilterSectionType.Tracks);

        return new FilterConditions
        (
                range.getStartDate(),
                range.getEndDate(),
                filtersOnEventTypes,
                filtersOnSummitTypes,
                filtersOnTrackGroups,
                filtersOnTracks,
                filtersOnTags,
                filtersOnLevels,
                filtersOnVenues,
                filtersOnRooms,
                showVideoTalks
        );
    }
}
