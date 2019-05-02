package org.openstack.android.summit.common.user_interface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
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
import org.openstack.android.summit.common.filters.DateRangeCondition;
import org.openstack.android.summit.common.filters.FilterConditionsBuilder;
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
        currentSummit = interactor.getActiveSummit();
        selectedDay = (savedInstanceState != null) ?
                savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_DAY, 0) :
                wireframe.getParameter(Constants.NAVIGATION_PARAMETER_DAY, Integer.class);

        if(selectedDay == null)
            selectedDay = 0;

        // if there isnt selected day and we are not on summit time, default date is
        if(selectedDay == 0 && currentSummit != null && !currentSummit.isCurrentDateTimeInsideSummitRange()){
            selectedDay = this.currentSummit.getScheduleStartDay();
        }

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
        view.setStartAndEndDateWithDisabledDates(startDate, endDate, inactiveDates);

        DateTime formerSelectedDate       = view.getSelectedDate();
        boolean currentDateInactive       = false;

        for (DateTime dt : inactiveDates) {
            if (formerSelectedDate != null && dt.compareTo(formerSelectedDate) == 0) {
                currentDateInactive = true;
            }
        }

        // is current date is inactive, move the current date to next active day ...
        if(formerSelectedDate != null && currentDateInactive){
            DateTime firstDate = currentSummit.getFirstEnabledDate(inactiveDates);
            this.selectedDay = (firstDate != null) ? firstDate.getDayOfMonth() : 0;
            view.setSelectedDate(this.selectedDay, false);
        }

        view.hideActivityIndicator();
    }

    @Override
    public void gotoNowOnSchedule(){
        this.gotoNowOnSchedule(false);
    }

    @Override
    public void gotoNowOnSchedule(boolean isFromButton){
        currentSummit = interactor.getActiveSummit();
        if (currentSummit == null) return;
        int summitCurrentDay = currentSummit.getCurrentLocalTime().withTime(0, 0, 0, 0).getDayOfMonth();
        view.setSelectedDate(summitCurrentDay, false);

        reloadSchedule(summitCurrentDay);

        if((this.dayEvents == null || this.dayEvents.isEmpty()) && isFromButton){
            AlertDialog dialog = AlertsBuilder.buildValidationError(view.getFragmentActivity(), view.getResources().getString(R.string.error_events_has_ended));
            if(dialog != null) dialog.show();
            return;
        }

        DateTime now                    = currentSummit.getCurrentLocalTime();
        int candidatePos                = -1;
        DateTime candidateEndDate       = null;
        boolean candidateAlreadyStarted = false;
        int candidatePosFallback        = -1;

        for(int i =0 ; i < dayEvents.size() ; i++) {
            ScheduleItemDTO item =  dayEvents.get(i);
            if
            (
                // not finished
                (item.getEndDate().isAfter(now.getMillis()) || item.getEndDate().isEqual(now.getMillis())) &&
                // presentation
                item.isPresentation()
            )
            {
                // already started
                boolean currentAlreadyStarted = (item.getStartDate().isBefore(now.getMillis()) || item.getStartDate().isEqual(now.getMillis()));
                if(currentAlreadyStarted &&
                   // started over more than hour ago
                  (((now.getMillis() - item.getStartDate().getMillis())/ 1000) > 3600 )) {
                    // save it just in case that its only available now event
                    candidatePosFallback = i;
                    continue;// exclude it
                }

                // if our candidate already started but our current didnt then exclude it
                if(candidateAlreadyStarted && !currentAlreadyStarted) continue;

                if(candidateEndDate == null || candidateEndDate.isAfter(item.getEndDate())) {
                    candidatePos            = i;
                    candidateAlreadyStarted = currentAlreadyStarted;
                    candidateEndDate        = item.getEndDate();
                }
            }
        }

        // if we dont have any pos set, then use fallback
        if(candidatePos < 0 ){
            candidatePos = candidatePosFallback;
        }

        if(candidatePos < 0 && isFromButton){
            // if all events finished then set last one as current one
            view.setListPosition(dayEvents.size() - 1);
            AlertDialog dialog = AlertsBuilder.buildValidationError(view.getFragmentActivity(), view.getResources().getString(R.string.error_events_has_ended));
            if(dialog != null) dialog.show();
            return;
        }

        view.setListPosition(candidatePos);
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
            // go to summit current day the first time that activity is created if we are on summit
            // time
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

        List<DateTime> inactiveDates = interactor.getDatesWithoutEvents
        (
            FilterConditionsBuilder.build(new DateRangeCondition(startDate, endDate), scheduleFilter)
        );
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