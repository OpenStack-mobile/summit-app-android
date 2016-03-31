package org.openstack.android.summit.common.user_interface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.IScheduleWireframe;
import org.openstack.android.summit.common.business_logic.IInteractorAsyncOperationListener;
import org.openstack.android.summit.common.business_logic.IScheduleInteractor;
import org.openstack.android.summit.common.business_logic.InteractorAsyncOperationListener;
import org.openstack.android.summit.modules.general_schedule_filter.user_interface.FilterSectionType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Claudio Redi on 12/28/2015.
 */
public abstract class SchedulePresenter<V extends IScheduleView, I extends IScheduleInteractor, W extends IScheduleWireframe> extends BasePresenter<V, I, W> implements ISchedulePresenter<V> {
    protected List<ScheduleItemDTO> dayEvents;
    protected IScheduleFilter scheduleFilter;
    protected InteractorAsyncOperationListener<ScheduleItemDTO> scheduleItemDTOIInteractorOperationListener;
    private IScheduleItemViewBuilder scheduleItemViewBuilder;
    protected IScheduleablePresenter scheduleablePresenter;
    private boolean isFirstTime = true;
    protected boolean hasToCheckDisabledDates = true;
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onResume();
        }
    };

    public SchedulePresenter(I interactor, W wireframe, IScheduleablePresenter scheduleablePresenter, IScheduleItemViewBuilder scheduleItemViewBuilder, IScheduleFilter scheduleFilter) {
        super(interactor, wireframe);

        this.scheduleablePresenter = scheduleablePresenter;
        this.scheduleItemViewBuilder = scheduleItemViewBuilder;
        this.scheduleFilter = scheduleFilter;
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
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.LOGGED_IN_EVENT);
        intentFilter.addAction(Constants.LOGGED_OUT_EVENT);
        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).registerReceiver(messageReceiver, intentFilter);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        view.showActivityIndicator();

        InteractorAsyncOperationListener<SummitDTO> summitDTOIInteractorOperationListener = new InteractorAsyncOperationListener<SummitDTO>() {
            @Override
            public void onSucceedWithData(SummitDTO data) {
                DateTimeZone summitTimeZone = DateTimeZone.forID(data.getTimeZone());
                DateTime startDate = new DateTime(data.getStartDate(), summitTimeZone).withTime(0, 0, 0, 0);
                DateTime endDate = new DateTime(data.getEndDate(), summitTimeZone).withTime(23, 59, 59, 999);
                List<DateTime> inactiveDates = hasToCheckDisabledDates || scheduleFilter.hasActiveFilters() ? getDatesWithoutEvents(startDate, endDate) : new ArrayList<DateTime>();
                if (isFirstTime) {
                    view.setStartAndEndDateWithDisabledDates(startDate, endDate, inactiveDates);
                }
                else {
                    view.setDisabledDates(inactiveDates);
                }
                isFirstTime = false;
                reloadSchedule();
                view.hideActivityIndicator();
                interactor.subscribeToPushChannelsUsingContextIfNotDoneAlready();
            }

            @Override
            public void onError(String message) {
                onFailedInitialLoad(message);
            }
        };

        interactor.getActiveSummit(summitDTOIInteractorOperationListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).unregisterReceiver(messageReceiver);
    }

    protected List<DateTime> getDatesWithoutEvents(DateTime startDate, DateTime endDate) {
        List<Integer> filtersOnEventTypes = (List<Integer>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.EventType);
        List<Integer> filtersOnTrackGroups = (List<Integer>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.TrackGroup);
        List<Integer> filtersOnSummitTypes = (List<Integer>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.SummitType);
        List<String> filtersOnLevels = (List<String>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.Level);
        List<String> filtersOnTags = (List<String>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.Tag);

        List<DateTime> inactiveDates = interactor.getDatesWithoutEvents(
                startDate,
                endDate,
                filtersOnEventTypes,
                filtersOnSummitTypes,
                filtersOnTrackGroups,
                null,
                filtersOnTags,
                filtersOnLevels);

        return inactiveDates;
    }

    protected void onFailedInitialLoad(String message) {
        view.hideActivityIndicator();
        view.showErrorMessage(message);
    }

    public void buildItem(IScheduleItemView scheduleItemView, int position) {
        ScheduleItemDTO scheduleItemDTO = dayEvents.get(position);
        scheduleItemViewBuilder.build(
                scheduleItemView,
                scheduleItemDTO,
                interactor.isMemberLoggedInConfirmedAttendee(),
                interactor.isEventScheduledByLoggedMember(scheduleItemDTO.getId()),
                false,
                interactor.shouldShowVenues()
        );
    }

    public void reloadSchedule() {
        DateTime selectedDate = view.getSelectedDate();

        if (selectedDate != null) {
            DateTime startDate = selectedDate.withTime(0, 0, 0, 0);
            DateTime endDate = selectedDate.withTime(23, 59, 59, 999);

            dayEvents = getScheduleEvents(startDate, endDate, interactor);
            view.setEvents(dayEvents);
            view.reloadSchedule();
        }
    }

    protected abstract List<ScheduleItemDTO> getScheduleEvents(DateTime startDate, DateTime endDate, I interactor);

    public void toggleScheduleStatus(IScheduleItemView scheduleItemView, int position) {
        ScheduleItemDTO scheduleItemDTO = dayEvents.get(position);

        IInteractorAsyncOperationListener<Void> interactorAsyncOperationListener = new InteractorAsyncOperationListener<Void>() {
            @Override
            public void onError(String message) {
                view.showErrorMessage(message);
            }
        };

        scheduleablePresenter.toggleScheduledStatusForEvent(scheduleItemDTO, scheduleItemView, interactor, interactorAsyncOperationListener);
    }

    @Override
    public void showEventDetail(int position) {
        ScheduleItemDTO scheduleItemDTO = dayEvents.get(position);
        wireframe.showEventDetail(scheduleItemDTO.getId(), view);
    }
}