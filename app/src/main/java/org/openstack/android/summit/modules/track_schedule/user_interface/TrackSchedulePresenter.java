package org.openstack.android.summit.modules.track_schedule.user_interface;

import android.os.Bundle;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.filters.DateRangeCondition;
import org.openstack.android.summit.common.filters.FilterConditionsBuilder;
import org.openstack.android.summit.common.user_interface.IScheduleItemViewBuilder;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.SchedulePresenter;
import org.openstack.android.summit.modules.general_schedule_filter.user_interface.FilterSectionType;
import org.openstack.android.summit.modules.track_schedule.ITrackScheduleWireframe;
import org.openstack.android.summit.modules.track_schedule.business_logic.ITrackScheduleInteractor;
import java.util.List;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public class TrackSchedulePresenter extends SchedulePresenter<ITrackScheduleView, ITrackScheduleInteractor, ITrackScheduleWireframe> implements ITrackSchedulePresenter {
    private Integer trackId;
    private NamedDTO track;

    public TrackSchedulePresenter(ITrackScheduleInteractor interactor, ITrackScheduleWireframe wireframe, IScheduleablePresenter scheduleablePresenter, IScheduleItemViewBuilder scheduleItemViewBuilder, IScheduleFilter scheduleFilter) {
        super(interactor, wireframe, scheduleablePresenter, scheduleItemViewBuilder, scheduleFilter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trackId = (savedInstanceState != null) ? savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_TRACK, 0) :
                wireframe.getParameter(Constants.NAVIGATION_PARAMETER_TRACK, Integer.class);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {

        track = interactor.getTrack(trackId);
        if(track != null)
            view.setTrack(track.getName());
        view.setShowActiveFilterIndicator(scheduleFilter.hasActiveFilters());

        super.onCreateView(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (trackId != null)
            outState.putInt(Constants.NAVIGATION_PARAMETER_TRACK, trackId);
    }

    @Override
    protected List<ScheduleItemDTO> getScheduleEvents(DateTime startDate, DateTime endDate, ITrackScheduleInteractor interactor) {

        scheduleFilter.getSelections().get(FilterSectionType.Tracks).clear();
        scheduleFilter.getSelections().get(FilterSectionType.Tracks).add(trackId);

        List<ScheduleItemDTO> summitEvents = interactor.getScheduleEvents(
                FilterConditionsBuilder.build(new DateRangeCondition(startDate, endDate), scheduleFilter)
        );

        return summitEvents;
    }

    @Override
    protected List<DateTime> getDatesWithoutEvents(DateTime startDate, DateTime endDate) {
        scheduleFilter.getSelections().get(FilterSectionType.Tracks).clear();
        scheduleFilter.getSelections().get(FilterSectionType.Tracks).add(trackId);

        List<DateTime> inactiveDates = interactor.getDatesWithoutEvents
        (
            FilterConditionsBuilder.build(new DateRangeCondition(startDate, endDate), scheduleFilter)
        );
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