package org.openstack.android.summit.common.user_interface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.crashlytics.android.Crashlytics;

import org.joda.time.DateTime;
import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.IScheduleWireframe;
import org.openstack.android.summit.common.business_logic.IScheduleInteractor;
import org.openstack.android.summit.common.business_logic.InteractorAsyncOperationListener;
import org.openstack.android.summit.modules.general_schedule_filter.user_interface.FilterSectionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Claudio Redi on 12/28/2015.
 */
public abstract class SchedulePresenter<V extends IScheduleView, I extends IScheduleInteractor, W
        extends IScheduleWireframe>
        extends BaseScheduleablePresenter<V, I, W> implements ISchedulePresenter<V> {

    protected boolean buttonNowState  = false;
    protected List<ScheduleItemDTO> dayEvents;
    protected IScheduleFilter scheduleFilter;
    protected InteractorAsyncOperationListener<ScheduleItemDTO> scheduleItemDTOIInteractorOperationListener;
    protected IScheduleItemViewBuilder scheduleItemViewBuilder;

    protected boolean hasToCheckDisabledDates = true;

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onResume();
        }
    };

    protected SummitDTO currentSummit = null;

    public SchedulePresenter
    (
            I interactor,
            W wireframe,
            IScheduleablePresenter scheduleablePresenter,
            IScheduleItemViewBuilder scheduleItemViewBuilder,
            IScheduleFilter scheduleFilter
    )
    {
        super(interactor, wireframe, scheduleablePresenter);
        this.scheduleItemViewBuilder = scheduleItemViewBuilder;
        this.scheduleFilter          = scheduleFilter;

        scheduleItemDTOIInteractorOperationListener = new InteractorAsyncOperationListener<ScheduleItemDTO>() {
            @Override
            public void onSucceedWithData(ScheduleItemDTO data) {
                super.onSucceedWithData(data);
            }

            @Override
            public void onError(String message) {
                super.onError(message);
            }
        };
    }

    @Override
    public void setView(V view) {
        this.view = view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState == null)
            buttonNowState = true;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.LOGGED_IN_EVENT);
        intentFilter.addAction(Constants.LOGGED_OUT_EVENT);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if (!interactor.isDataLoaded()) return;
        currentSummit = interactor.getActiveSummit();
        if (currentSummit == null) return;

        if (currentSummit.isCurrentDateTimeInsideSummitRange() && buttonNowState) {
            view.clearNowButtonListener();
            view.setNowButtonState(buttonNowState);
            buttonNowState = false;
            view.setNowButtonListener();
        }

        setRangerState();
        reloadSchedule();
    }

    @Override
    public void setRangerState() {

        currentSummit = interactor.getActiveSummit();
        if (currentSummit == null) return;

        DateTime startDate          = currentSummit.getLocalStartDate().withTime(0, 0, 0, 0);
        DateTime endDate            = currentSummit.getLocalEndDate().withTime(23, 59, 59, 999);
        boolean shouldHidePastTalks = view.getNowButtonState();

        view.setNowButtonVisibility(currentSummit.isCurrentDateTimeInsideSummitRange() ? View.VISIBLE : View.GONE);


        // check if current time is on summit time
        List<DateTime> pastDates     = shouldHidePastTalks ? currentSummit.getPastDates() : new ArrayList<DateTime>();
        List<DateTime> inactiveDates = hasToCheckDisabledDates || scheduleFilter.hasActiveFilters() ? getDatesWithoutEvents(startDate, endDate) : new ArrayList<DateTime>();
        // now merge past dates with inactive dates
        inactiveDates.removeAll(pastDates);
        inactiveDates.addAll(pastDates);
        Collections.sort(inactiveDates);
        // set ranger states
        view.setStartAndEndDateWithDisabledDates(startDate, endDate, inactiveDates);
        DateTime formerSelectedDate       = view.getSelectedDate();
        DateTime scheduleStartDate        = currentSummit.getLocalScheduleStartDate();
        boolean scheduleStartDateInactive = false;

        for (DateTime dt : inactiveDates) {
            if (dt.compareTo(scheduleStartDate) == 0) {
                scheduleStartDateInactive = true;
                break;
            }
        }

        if (!scheduleStartDateInactive && formerSelectedDate == null) {
            view.setSelectedDate(currentSummit.getScheduleStartDay(), false);
        }

        if (currentSummit.isCurrentDateTimeInsideSummitRange()) {
            int summitCurrentDay = currentSummit.getCurrentLocalTime().withTime(0, 0, 0, 0).getDayOfMonth();
            if(shouldHidePastTalks || formerSelectedDate == null)
                view.setSelectedDate(summitCurrentDay, false);
        }
        view.hideActivityIndicator();
    }

    @Override
    public void onResume() {
        try {
            super.onResume();
            setRangerState();
        } catch (Exception ex) {
            Crashlytics.logException(ex);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).unregisterReceiver(messageReceiver);
    }

    protected List<DateTime> getDatesWithoutEvents(DateTime startDate, DateTime endDate) {

        List<Integer> filtersOnEventTypes  = (List<Integer>) (List<?>) scheduleFilter.getSelections().get(FilterSectionType.EventType);
        List<Integer> filtersOnTrackGroups = (List<Integer>) (List<?>) scheduleFilter.getSelections().get(FilterSectionType.TrackGroup);
        List<Integer> filtersOnSummitTypes = (List<Integer>) (List<?>) scheduleFilter.getSelections().get(FilterSectionType.SummitType);
        List<String> filtersOnLevels       = (List<String>) (List<?>) scheduleFilter.getSelections().get(FilterSectionType.Level);
        List<String> filtersOnTags         = (List<String>) (List<?>) scheduleFilter.getSelections().get(FilterSectionType.Tag);
        List<Integer> filtersOnVenues      = (List<Integer>) (List<?>) scheduleFilter.getSelections().get(FilterSectionType.Venues);

        List<DateTime> inactiveDates = interactor.getDatesWithoutEvents(
                startDate,
                endDate,
                filtersOnEventTypes,
                filtersOnSummitTypes,
                filtersOnTrackGroups,
                null,
                filtersOnTags,
                filtersOnLevels,
                filtersOnVenues);

        return inactiveDates;
    }

    public void buildItem(IScheduleItemView scheduleItemView, int position) {
        if (dayEvents.size() - 1 < position) return;
        ScheduleItemDTO scheduleItemDTO = dayEvents.get(position);
        scheduleItemViewBuilder.build
        (
            scheduleItemView,
            scheduleItemDTO,
            interactor.isMemberLoggedIn(),
            interactor.isMemberLoggedInAndConfirmedAttendee(),
            interactor.isEventScheduledByLoggedMember(scheduleItemDTO.getId()),
            interactor.isEventFavoriteByLoggedMember(scheduleItemDTO.getId()),
            false,
            interactor.shouldShowVenues(),
            scheduleItemDTO.getRSVPLink() ,
            scheduleItemDTO.isExternalRSVP()
        );
    }

    private DateTime getStartDateFilter(DateTime selectedDate) {
        boolean hidePastTalks = view.getNowButtonState();
        boolean selectedDayIsToday = selectedDate.isEqual(currentSummit.getCurrentLocalTime().withTime(0, 0, 0, 0));
        int hour   = (hidePastTalks && currentSummit.isCurrentDateTimeInsideSummitRange() && selectedDayIsToday) ? currentSummit.getCurrentLocalTime().getHourOfDay() : 0;
        int minute = (hidePastTalks && currentSummit.isCurrentDateTimeInsideSummitRange() && selectedDayIsToday) ? currentSummit.getCurrentLocalTime().getMinuteOfHour() : 0;
        int second = (hidePastTalks && currentSummit.isCurrentDateTimeInsideSummitRange() && selectedDayIsToday) ? currentSummit.getCurrentLocalTime().getSecondOfMinute() : 0;
        int millis = (hidePastTalks && currentSummit.isCurrentDateTimeInsideSummitRange() && selectedDayIsToday) ? currentSummit.getCurrentLocalTime().getMillisOfSecond() : 0;

        return selectedDate.withTime(hour, minute, second, millis);
    }

    @Override
    public void reloadSchedule() {

        DateTime selectedDate = view.getSelectedDate();
        if (selectedDate != null) {
            view.showEmptyMessage(false);

            DateTime startDate = getStartDateFilter(selectedDate);
            DateTime endDate   = selectedDate.withTime(23, 59, 59, 999);

            dayEvents = getScheduleEvents(startDate, endDate, interactor);
            view.setEvents(dayEvents);
            view.reloadSchedule();
            return;
        }
        view.showEmptyMessage(true);
    }

    protected abstract List<ScheduleItemDTO> getScheduleEvents(DateTime startDate, DateTime endDate, I interactor);

    @Override
    public void toggleScheduleStatus(IScheduleItemView scheduleItemView, int position) {
        _toggleScheduleStatus(scheduleItemView, position);
    }

    @Override
    public void toggleFavoriteStatus(IScheduleItemView scheduleItemView, int position) {
        _toggleFavoriteStatus(scheduleItemView, position);
    }

    @Override
    public void toggleRSVPStatus(IScheduleItemView scheduleItemView, int position) {
       _toggleRSVPStatus(scheduleItemView, position);
    }

    @Override
    public void shareEvent(IScheduleItemView scheduleItemView, int position){
        _shareEvent(scheduleItemView, position);
    }

    @Override
    public void showEventDetail(int position) {
        if (dayEvents.size() - 1 < position) return;
        ScheduleItemDTO scheduleItemDTO = dayEvents.get(position);

        if (interactor.eventExist(scheduleItemDTO.getId())) {
            wireframe.showEventDetail(scheduleItemDTO.getId(), view);
            return;
        }

        view.showErrorMessage(view.getResources().getString(R.string.event_not_exist));
        onResume();
    }

    @Override
    protected ScheduleItemDTO getCurrentItem(int position){
        if (dayEvents.size() - 1 < position) return null;
        return dayEvents.get(position);
    }

    @Override
    public void setHidePastTalks(boolean hidePastTalks) {
        setRangerState();
        reloadSchedule();
    }
}