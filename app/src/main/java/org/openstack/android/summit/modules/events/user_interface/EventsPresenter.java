package org.openstack.android.summit.modules.events.user_interface;

import android.os.Bundle;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.user_interface.AlertsBuilder;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.events.IEventsWireframe;
import org.openstack.android.summit.modules.events.business_logic.IEventsInteractor;
import org.openstack.android.summit.modules.general_schedule_filter.user_interface.FilterSectionType;

import javax.inject.Inject;

/**
 * Created by claudio on 10/30/2015.
 */
public class EventsPresenter extends BasePresenter<IEventsView, IEventsInteractor, IEventsWireframe> implements IEventsPresenter {

    private int selectedTabIndex;
    private final String KEY_SELECTED_TAB_INDEX = "KEY_SELECTED_TAB_INDEX";
    private IScheduleFilter scheduleFilter;

    @Inject
    public EventsPresenter(IEventsInteractor eventsInteractor, IEventsWireframe wireframe, IScheduleFilter scheduleFilter)
    {
        super(eventsInteractor, wireframe);
        this.scheduleFilter = scheduleFilter;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_SELECTED_TAB_INDEX, selectedTabIndex);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            selectedTabIndex = savedInstanceState.getInt(KEY_SELECTED_TAB_INDEX);
        }
        view.setShowActiveFilterIndicator(scheduleFilter.hasActiveFilters());
    }

    @Override
    public void onResume() {
        super.onResume();
        // set the default enabled filter
        view.setShowActiveFilterIndicator(scheduleFilter.hasActiveFilters());
    }

    @Override
    public void showFilterView() {
        if(!interactor.isDataLoaded()) {
            AlertsBuilder.buildError(view.getFragmentActivity(), R.string.no_summit_data_available).show();
            return;
        }
        wireframe.showFilterView(view);
    }

    @Override
    public void clearFilters() {
        scheduleFilter.clearActiveFilters();
        wireframe.presentEventsView(view);
    }
}
