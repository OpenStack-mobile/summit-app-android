package org.openstack.android.summit.modules.level_schedule.user_interface;

import android.os.Bundle;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.user_interface.IScheduleItemViewBuilder;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.SchedulePresenter;
import org.openstack.android.summit.modules.general_schedule_filter.user_interface.FilterSectionType;
import org.openstack.android.summit.modules.level_schedule.ILevelScheduleWireframe;
import org.openstack.android.summit.modules.level_schedule.business_logic.ILevelScheduleInteractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public class LevelSchedulePresenter
        extends SchedulePresenter<ILevelScheduleView, ILevelScheduleInteractor, ILevelScheduleWireframe>
        implements ILevelSchedulePresenter {

    private String level;

    public LevelSchedulePresenter(ILevelScheduleInteractor interactor, ILevelScheduleWireframe wireframe, IScheduleablePresenter scheduleablePresenter, IScheduleItemViewBuilder scheduleItemViewBuilder, IScheduleFilter scheduleFilter) {
        super(interactor, wireframe, scheduleablePresenter, scheduleItemViewBuilder, scheduleFilter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        level = (savedInstanceState != null) ?
                savedInstanceState.getString(Constants.NAVIGATION_PARAMETER_LEVEL, "") :
                wireframe.getParameter(Constants.NAVIGATION_PARAMETER_LEVEL, String.class);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {

        view.setShowActiveFilterIndicator(scheduleFilter.hasActiveFilters());
        super.onCreateView(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        view.setTitle(level.toUpperCase() + " LEVEL");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(level != null)
            outState.putString(Constants.NAVIGATION_PARAMETER_LEVEL, level);
    }

    @Override
    protected List<ScheduleItemDTO> getScheduleEvents(DateTime startDate, DateTime endDate, ILevelScheduleInteractor interactor) {

        List<Integer> filtersOnEventTypes  = (List<Integer>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.EventType);
        List<Integer> filtersOnTrackGroups = (List<Integer>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.TrackGroup);
        List<Integer> filtersOnSummitTypes = (List<Integer>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.SummitType);
        List<String> filtersOnLevels       = (List<String>)(List<?>)  scheduleFilter.getSelections().get(FilterSectionType.Level);
        List<String> filtersOnTags         = (List<String>)(List<?>)  scheduleFilter.getSelections().get(FilterSectionType.Tag);
        List<Integer> filtersOnVenues      = (List<Integer>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.Venues);

        if (filtersOnLevels != null && filtersOnLevels.size() > 0 && !filtersOnLevels.contains(level)) {
            return new ArrayList<>();
        }

        ArrayList<String> levels = new ArrayList<>();
        levels.add(level);
        List<ScheduleItemDTO> summitEvents = interactor.getScheduleEvents(
                startDate,
                endDate,
                filtersOnEventTypes,
                filtersOnSummitTypes,
                filtersOnTrackGroups,
                null,
                filtersOnTags,
                levels,
                filtersOnVenues
        );

        return summitEvents;
    }

    @Override
    protected List<DateTime> getDatesWithoutEvents(DateTime startDate, DateTime endDate) {

        List<Integer> filtersOnEventTypes  = (List<Integer>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.EventType);
        List<Integer> filtersOnTrackGroups = (List<Integer>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.TrackGroup);
        List<Integer> filtersOnSummitTypes = (List<Integer>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.SummitType);
        List<String> filtersOnLevels       = (List<String>)(List<?>)  scheduleFilter.getSelections().get(FilterSectionType.Level);
        List<String> filtersOnTags         = (List<String>)(List<?>)  scheduleFilter.getSelections().get(FilterSectionType.Tag);
        List<Integer> filtersOnVenues      = (List<Integer>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.Venues);

        if (filtersOnLevels != null && filtersOnLevels.size() > 0 && !filtersOnLevels.contains(level)) {
            return new ArrayList<>();
        }

        ArrayList<String> levels = new ArrayList<>();
        levels.add(level);

        List<DateTime> inactiveDates = interactor.getDatesWithoutEvents(
                startDate,
                endDate,
                filtersOnEventTypes,
                filtersOnSummitTypes,
                filtersOnTrackGroups,
                null,
                filtersOnTags,
                levels,
                filtersOnVenues);

        return inactiveDates;
    }

    @Override
    public void showFilterView() {
        wireframe.showFilterView(view);
    }

    @Override
    public void clearFilters() {
        scheduleFilter.clearActiveFilters();
        onCreateView(null);
        onResume();
    }
}
