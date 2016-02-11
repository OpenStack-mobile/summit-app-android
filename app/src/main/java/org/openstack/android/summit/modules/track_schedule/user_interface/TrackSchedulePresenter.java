package org.openstack.android.summit.modules.track_schedule.user_interface;

import android.os.Bundle;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.user_interface.IScheduleItemViewBuilder;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.SchedulePresenter;
import org.openstack.android.summit.modules.general_schedule_filter.user_interface.FilterSectionType;
import org.openstack.android.summit.modules.track_schedule.ITrackScheduleWireframe;
import org.openstack.android.summit.modules.track_schedule.business_logic.ITrackScheduleInteractor;

import java.util.ArrayList;
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
    public void onCreateView(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            trackId = savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_TRACK);
        }
        else {
            trackId = wireframe.getParameter(Constants.NAVIGATION_PARAMETER_TRACK, Integer.class);
        }

        track = interactor.getTrack(trackId);
        view.setTrack(track.getName());
        view.setShowActiveFilterIndicator(scheduleFilter.hasActiveFilters());

        super.onCreateView(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.NAVIGATION_PARAMETER_TRACK, trackId);
    }

    @Override
    protected List<ScheduleItemDTO> getScheduleEvents(DateTime startDate, DateTime endDate, ITrackScheduleInteractor interactor) {
        List<Integer> filtersOnEventTypes = (List<Integer>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.EventType);
        List<Integer> filtersOnTrackGroups = (List<Integer>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.TrackGroup);
        List<Integer> filtersOnSummitTypes = (List<Integer>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.SummitType);
        List<String> filtersOnLevels = (List<String>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.Level);
        List<String> filtersOnTags = (List<String>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.Tag);

        ArrayList<Integer> tracks = new ArrayList<>();
        tracks.add(trackId);
        List<ScheduleItemDTO> summitEvents = interactor.getScheduleEvents(
                startDate.toDate(),
                endDate.toDate(),
                filtersOnEventTypes,
                filtersOnSummitTypes,
                filtersOnTrackGroups,
                tracks,
                filtersOnTags,
                filtersOnLevels
        );

        return summitEvents;
    }

    @Override
    public void showFilterView() {
        wireframe.showFilterView(view);
    }

    @Override
    public void clearFilters() {
        scheduleFilter.clearActiveFilters();
        onCreate(null);
    }
}