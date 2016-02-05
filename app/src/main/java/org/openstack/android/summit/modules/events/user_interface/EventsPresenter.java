package org.openstack.android.summit.modules.events.user_interface;

import android.os.Bundle;

import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.events.IEventsWireframe;

import javax.inject.Inject;

/**
 * Created by claudio on 10/30/2015.
 */
public class EventsPresenter extends BasePresenter<IEventsView, Void, IEventsWireframe> implements IEventsPresenter {

    private int selectedTabIndex;
    private final String KEY_SELECTED_TAB_INDEX = "KEY_SELECTED_TAB_INDEX";
    private IScheduleFilter scheduleFilter;

    @Inject
    public EventsPresenter(IEventsWireframe wireframe, IScheduleFilter scheduleFilter)
    {
        super(null, wireframe);
        this.scheduleFilter = scheduleFilter;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_SELECTED_TAB_INDEX, selectedTabIndex);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            selectedTabIndex = savedInstanceState.getInt(KEY_SELECTED_TAB_INDEX);
        }

        view.setShowActiveFilterIndicator(scheduleFilter.hasActiveFilters());
    }

    @Override
    public void showFilterView() {
        wireframe.showFilterView(view);
    }

    @Override
    public void clearFilters() {
        scheduleFilter.clearActiveFilters();
        wireframe.presentEventsView(view);
    }
}
