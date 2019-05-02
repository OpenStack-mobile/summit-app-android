package org.openstack.android.summit.modules.general_schedule.user_interface;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;

import org.joda.time.DateTime;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.filters.DateRangeCondition;
import org.openstack.android.summit.common.filters.FilterConditionsBuilder;
import org.openstack.android.summit.common.user_interface.AlertsBuilder;
import org.openstack.android.summit.common.user_interface.IScheduleItemViewBuilder;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.SchedulePresenter;
import org.openstack.android.summit.modules.general_schedule.IGeneralScheduleWireframe;
import org.openstack.android.summit.modules.general_schedule.business_logic.IGeneralScheduleInteractor;
import org.openstack.android.summit.modules.general_schedule_filter.user_interface.FilterSectionType;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 12/21/2015.
 */
public class GeneralSchedulePresenter
        extends SchedulePresenter<GeneralScheduleFragment, IGeneralScheduleInteractor, IGeneralScheduleWireframe>
        implements IGeneralSchedulePresenter {

    @Inject
    public GeneralSchedulePresenter
    (
        IGeneralScheduleInteractor interactor,
        IGeneralScheduleWireframe wireframe,
        IScheduleablePresenter scheduleablePresenter,
        IScheduleItemViewBuilder scheduleItemViewBuilder,
        IScheduleFilter scheduleFilter
    )
    {
        super(interactor, wireframe, scheduleablePresenter, scheduleItemViewBuilder, scheduleFilter);
        hasToCheckDisabledDates = true;
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        view.setShowActiveFilterIndicator(scheduleFilter.hasActiveFilters());
    }
    @Override
    public void onResume() {
        view.toggleEventList(true);
        super.onResume();
        // reset hide past talks filter state
        if (currentSummit != null && !currentSummit.isCurrentDateTimeInsideSummitRange()){
            scheduleFilter.clearTypeValues(FilterSectionType.HidePastTalks);
        }
        view.setShowActiveFilterIndicator(scheduleFilter.hasActiveFilters());
    }

    @Override
    protected List<ScheduleItemDTO> getScheduleEvents(DateTime startDate, DateTime endDate, IGeneralScheduleInteractor interactor) {

        List<ScheduleItemDTO> events = interactor.getScheduleEvents(
            FilterConditionsBuilder.build(new DateRangeCondition(startDate, endDate), scheduleFilter)
        );
        return events;
    }

    @Override
    public void showFilterView() {
        if(!interactor.isDataLoaded()) {
            AlertDialog dialog = AlertsBuilder.buildError(view.getFragmentActivity(), R.string.no_summit_data_available);
            if(dialog != null) dialog.show();
            return;
        }
        wireframe.showFilterView(view);
    }

    @Override
    public void clearFilters() {
        scheduleFilter.clearActiveFilters();
        view.setShowActiveFilterIndicator(false);
        setRangerState();
        reloadSchedule();
    }
}