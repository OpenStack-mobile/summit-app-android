package org.openstack.android.summit.common.user_interface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
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
import org.openstack.android.summit.modules.general_schedule_filter.user_interface.FilterSectionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Claudio Redi on 12/28/2015.
 */
public abstract class SchedulePresenter<V extends IScheduleView, I extends IScheduleInteractor, W
        extends IScheduleWireframe>
        extends BaseScheduleablePresenter<V, I, W> implements ISchedulePresenter<V> {

    protected boolean setNowButtonInitialState = false;
    protected List<ScheduleItemDTO> dayEvents;
    protected Map<Integer,Integer> eventPosIds = new HashMap<>();
    protected IScheduleFilter scheduleFilter;
    protected IScheduleItemViewBuilder scheduleItemViewBuilder;
    protected Integer selectedDay             = null;
    protected boolean hasToCheckDisabledDates = true;
    protected boolean shouldShowNow           = true;

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {

                if (    intent.getAction() == Constants.DATA_UPDATE_UPDATED_ENTITY_EVENT
                        || intent.getAction() == Constants.DATA_UPDATE_MY_SCHEDULE_EVENT_ADDED
                        || intent.getAction() == Constants.DATA_UPDATE_MY_SCHEDULE_EVENT_DELETED
                        || intent.getAction() == Constants.DATA_UPDATE_MY_FAVORITE_EVENT_ADDED
                        || intent.getAction() == Constants.DATA_UPDATE_MY_FAVORITE_EVENT_DELETED
                        ) {
                    int entityId = intent.getIntExtra(Constants.DATA_UPDATE_ENTITY_ID, 0);
                    String entityClassName = intent.getStringExtra(Constants.DATA_UPDATE_ENTITY_CLASS);

                    if(entityId > 0 && eventPosIds.containsKey(entityId) && !dayEvents.isEmpty()){
                        int pos                    = eventPosIds.get(entityId);
                        if (dayEvents.size() - 1 < pos || pos < 0) return;
                        ScheduleItemDTO updateItem = interactor.getEvent(entityId);
                        if(updateItem == null) return;

                        dayEvents.set(pos, updateItem);

                        view.getFragmentActivity().runOnUiThread( () -> {
                            view.reloadItem(pos);
                        });
                    }
                }

            } catch (Exception ex) {
                Crashlytics.logException(new Exception(String.format("Action %s", intent.getAction()), ex));
            }
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
    }

    @Override
    public void setView(V view) {
        this.view = view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        selectedDay = (savedInstanceState != null) ?
                savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_DAY, 0) :
                wireframe.getParameter(Constants.NAVIGATION_PARAMETER_DAY, Integer.class);

        if(savedInstanceState != null)
            setNowButtonInitialState = savedInstanceState.getBoolean(Constants.SETTING_SET_NOW_BUTTON_INITIAL_STATE);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.LOGGED_IN_EVENT);
        intentFilter.addAction(Constants.LOGGED_OUT_EVENT);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(selectedDay != null)
            outState.putInt(Constants.NAVIGATION_PARAMETER_DAY, selectedDay);
        if(view.getSelectedDay() > 0 )
            outState.putInt(Constants.NAVIGATION_PARAMETER_DAY, view.getSelectedDay());
        outState.putBoolean(Constants.SETTING_SET_NOW_BUTTON_INITIAL_STATE, setNowButtonInitialState);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if (!interactor.isDataLoaded()) return;

        if (selectedDay != null && selectedDay > 0 ){
            view.setSelectedDate(selectedDay, false);
        }
        // set initial state;
        setRangerState();
        reloadSchedule();
    }

    @Override
    public void setRangerState() {

        currentSummit = interactor.getActiveSummit();
        if (currentSummit == null) return;

        DateTime startDate          = currentSummit.getLocalStartDate().withTime(0, 0, 0, 0);
        DateTime endDate            = currentSummit.getLocalEndDate().withTime(23, 59, 59, 999);
        boolean shouldHidePastTalks = this.shouldHidePastTalks();

        view.setNowButtonVisibility(currentSummit.isCurrentDateTimeInsideSummitRange() ? View.VISIBLE : View.GONE);

        // check if current time is on summit time
        List<DateTime> pastDates     = shouldHidePastTalks ? currentSummit.getPastDates() : new ArrayList<>();
        List<DateTime> inactiveDates = hasToCheckDisabledDates || scheduleFilter.hasActiveFilters() ? getDatesWithoutEvents(startDate, endDate) : new ArrayList<>();
        // now merge past dates with inactive dates
        inactiveDates.removeAll(pastDates);
        inactiveDates.addAll(pastDates);
        Collections.sort(inactiveDates);
        // set ranger states
        DateTime formerSelectedDate       = view.getSelectedDate();
        view.setStartAndEndDateWithDisabledDates(startDate, endDate, inactiveDates);
        DateTime scheduleStartDate        = currentSummit.getLocalScheduleStartDate();
        boolean scheduleStartDateInactive = false;
        boolean currentDateInactive       = false;

        for (DateTime dt : inactiveDates) {
            if (dt.compareTo(scheduleStartDate) == 0) {
                scheduleStartDateInactive = true;
            }
            if (formerSelectedDate != null && dt.compareTo(formerSelectedDate) == 0) {
                currentDateInactive = true;
            }
        }

        // outside of summit time, the default schedule start date should be set
        if (!scheduleStartDateInactive && formerSelectedDate == null && !currentSummit.isCurrentDateTimeInsideSummitRange()) {
            this.selectedDay = currentSummit.getScheduleStartDay();
            view.setSelectedDate(this.selectedDay, false);
        }

        // is current date is inactive, move the current date to next active day ...
        if(formerSelectedDate != null && currentDateInactive){
            DateTime firstDate = currentSummit.getFirstEnabledDate(inactiveDates);
            if(firstDate != null){
                this.selectedDay = firstDate.getDayOfMonth();
                view.setSelectedDate(this.selectedDay, false);
            }
        }

        view.hideActivityIndicator();
    }

    @Override
    public void gotoNowOnSchedule() {
        currentSummit = interactor.getActiveSummit();
        if (currentSummit == null) return;
        int summitCurrentDay = currentSummit.getCurrentLocalTime().withTime(0, 0, 0, 0).getDayOfMonth();
        view.setSelectedDate(summitCurrentDay, false);

        reloadSchedule(summitCurrentDay);
        DateTime now              = currentSummit.getCurrentLocalTime();
        boolean  foundFirstsEvent = false;

        if(this.dayEvents == null || this.dayEvents.isEmpty()){
            AlertsBuilder.buildValidationError(view.getFragmentActivity(), view.getResources().getString(R.string.error_events_has_ended)).show();
            return;
        }

        int position = 0;
        for (ScheduleItemDTO item: dayEvents) {
            if
            (
                (item.getEndDate().isAfter(now.getMillis()) || item.getEndDate().isEqual(now.getMillis()))
                &&
                item.isPresentation()
            )
            {
                foundFirstsEvent = true;
                break;
            }
            position++;
        }
        if(!foundFirstsEvent){
            AlertsBuilder.buildValidationError(view.getFragmentActivity(), view.getResources().getString(R.string.error_events_has_ended)).show();
            return;
        }
        view.setListPosition(position);
    }

    @Override
    public void onResume() {
        try {
            super.onResume();
            // bind local broadcast receiver
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.DATA_UPDATE_UPDATED_ENTITY_EVENT);
            intentFilter.addAction(Constants.DATA_UPDATE_MY_SCHEDULE_EVENT_ADDED);
            intentFilter.addAction(Constants.DATA_UPDATE_MY_SCHEDULE_EVENT_DELETED);
            intentFilter.addAction(Constants.DATA_UPDATE_MY_FAVORITE_EVENT_ADDED);
            intentFilter.addAction(Constants.DATA_UPDATE_MY_FAVORITE_EVENT_DELETED);
            LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).registerReceiver(messageReceiver, intentFilter);
            currentSummit = interactor.getActiveSummit();
            // called only in case that we are doing a filtering, we need to set the ranger state again
            setRangerState();
            // show now the first time that activity is created
            if(currentSummit != null && currentSummit.isCurrentDateTimeInsideSummitRange() && shouldShowNow){
                gotoNowOnSchedule();
                shouldShowNow = false;
            }

        } catch (Exception ex) {
            Crashlytics.logException(ex);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).unregisterReceiver(messageReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        if (dayEvents == null || dayEvents.isEmpty() || (dayEvents.size() - 1) < position || position < 0) return;
        ScheduleItemDTO scheduleItemDTO = dayEvents.get(position);
        if(eventPosIds.containsKey(scheduleItemDTO.getId())){
            eventPosIds.remove(scheduleItemDTO.getId());
        }
        eventPosIds.put(scheduleItemDTO.getId(), position);
        scheduleItemViewBuilder.build
        (
            scheduleItemView,
            scheduleItemDTO,
            isMemberLogged,
            scheduleItemDTO.getScheduled(),
            scheduleItemDTO.getFavorite(),
            false,
            shouldShowVenues,
            scheduleItemDTO.getRSVPLink() ,
            scheduleItemDTO.isExternalRSVP(),
            scheduleItemDTO.getAllowFeedback(),
            scheduleItemDTO.isToRecord()
        );
    }

    private boolean shouldHidePastTalks(){
        List<Boolean> filtersOnPassTalks = (List<Boolean>)(List<?>)scheduleFilter.getSelections().get(FilterSectionType.HidePastTalks);
        return (filtersOnPassTalks != null && !filtersOnPassTalks.isEmpty()) ? filtersOnPassTalks.get(0) : false;
    }

    private DateTime getStartDateFilter(DateTime selectedDate) {
        boolean hidePastTalks      = shouldHidePastTalks();
        boolean selectedDayIsToday = selectedDate.isEqual(currentSummit.getCurrentLocalTime().withTime(0, 0, 0, 0));
        int hour   = (hidePastTalks && currentSummit.isCurrentDateTimeInsideSummitRange() && selectedDayIsToday) ? currentSummit.getCurrentLocalTime().getHourOfDay() : 0;
        int minute = (hidePastTalks && currentSummit.isCurrentDateTimeInsideSummitRange() && selectedDayIsToday) ? currentSummit.getCurrentLocalTime().getMinuteOfHour() : 0;
        int second = (hidePastTalks && currentSummit.isCurrentDateTimeInsideSummitRange() && selectedDayIsToday) ? currentSummit.getCurrentLocalTime().getSecondOfMinute() : 0;
        int millis = (hidePastTalks && currentSummit.isCurrentDateTimeInsideSummitRange() && selectedDayIsToday) ? currentSummit.getCurrentLocalTime().getMillisOfSecond() : 0;

        return selectedDate.withTime(hour, minute, second, millis);
    }

    @Override
    public void reloadSchedule(int day) {
        DateTime selectedDate = view.getSelectedDate();
        if(day > 0)
            this.selectedDay = day;
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

    @Override
    public void reloadSchedule() {
        reloadSchedule(0);
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
    public void rateEvent(IScheduleItemView scheduleItemView, int position) {
        _rateEvent(scheduleItemView, position);
    }

    @Override
    public void showEventDetail(int position) {
        if (dayEvents == null || dayEvents.isEmpty() || (dayEvents.size() - 1) < position || position <0) return;

        ScheduleItemDTO scheduleItemDTO = dayEvents.get(position);

        if (interactor.eventExist(scheduleItemDTO.getId())) {
            wireframe.showEventDetail(scheduleItemDTO.getId(), view);
            return;
        }

        AlertDialog dialog = AlertsBuilder.buildAlert(view.getFragmentActivity(),R.string.generic_info_title, R.string.event_not_exist);
        if(dialog != null) dialog.show();
        onResume();
    }

    @Override
    protected ScheduleItemDTO getCurrentItem(int position){
        if (dayEvents.size() - 1 < position || dayEvents.size() == 0 || position < 0) return null;
        return dayEvents.get(position);
    }

}