package org.openstack.android.summit.common.user_interface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import org.joda.time.DateTime;
import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.IScheduleWireframe;
import org.openstack.android.summit.common.business_logic.IScheduleInteractor;
import org.openstack.android.summit.common.business_logic.InteractorAsyncOperationListener;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Claudio Redi on 12/28/2015.
 */
public class SchedulePresenter<V extends ScheduleFragment, I extends IScheduleInteractor, W extends IScheduleWireframe> extends ScheduleablePresenter implements ISchedulePresenter<V, I , W> {
    V view;
    I interactor;
    W wireframe;
    List<ScheduleItemDTO> dayEvents;
    Date selectedDate;
    int summitTimeZoneOffset;
    protected InteractorAsyncOperationListener<ScheduleItemDTO> scheduleItemDTOIInteractorOperationListener;
    public SchedulePresenter(I interactor, W wireframe) {
        this.interactor = interactor;
        this.wireframe = wireframe;
        scheduleItemDTOIInteractorOperationListener = new InteractorAsyncOperationListener<ScheduleItemDTO>() {
            @Override
            public void onSuceedWithData(ScheduleItemDTO data) {
                super.onSuceedWithData(data);
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
        initialize(savedInstanceState);
    }

    private void initialize(Bundle savedInstanceState) {
        view.showActivityIndicator();

        InteractorAsyncOperationListener<SummitDTO> summitDTOIInteractorOperationListener = new InteractorAsyncOperationListener<SummitDTO>() {
            @Override
            public void onSuceedWithData(SummitDTO data) {
                summitTimeZoneOffset = TimeZone.getTimeZone(data.getTimeZone()).getOffset(new Date().getTime());
                DateTime startDate = new DateTime(data.getStartDate()).plus(summitTimeZoneOffset).withTime(0, 0, 0, 0);
                DateTime endDate = new DateTime(data.getEndDate()).plus(summitTimeZoneOffset).withTime(23, 59, 59, 999);

                view.setStartAndEndDateWithParts(
                        startDate.getYear(),
                        startDate.getMonthOfYear(),
                        startDate.getDayOfMonth(),
                        endDate.getYear(),
                        endDate.getMonthOfYear(),
                        endDate.getDayOfMonth()
                );
                reloadSchedule();
                view.hideActivityIndicator();
            }

            @Override
            public void onError(String message) {
                view.hideActivityIndicator();
                view.showErrorMessage(message);
            }
        };

        interactor.getActiveSummit(summitDTOIInteractorOperationListener);
    }

    public void buildItem(IScheduleItemView scheduleItemView, int position) {
        ScheduleItemDTO scheduleItemDTO = dayEvents.get(position);
        scheduleItemView.setName(scheduleItemDTO.getName());
        scheduleItemView.setTime(scheduleItemDTO.getTime());
        scheduleItemView.setSponsors(scheduleItemDTO.getSponsors());
        scheduleItemView.setEventType(scheduleItemDTO.getEventType().toUpperCase());
        scheduleItemView.setTrack(scheduleItemDTO.getTrack());
        scheduleItemView.setIsScheduledStatusVisible(interactor.isMemberLoggedIn());
        if (interactor.isMemberLoggedIn()) {
            scheduleItemView.setScheduled(interactor.isEventScheduledByLoggedMember(scheduleItemDTO.getId()));
        }
        String summitTypeColor = scheduleItemDTO.getSummitTypeColor() != "" ? scheduleItemDTO.getSummitTypeColor() : "#8A8A8A";
        scheduleItemView.setSummitTypeColor(summitTypeColor);
    }

    public void reloadSchedule() {
        selectedDate = view.getSelectedDate();

        int offsetLocalTimeZone  = TimeZone.getDefault().getOffset(new Date().getTime());

        DateTime startDate = new DateTime(view.getSelectedDate()).withTime(0, 0, 0, 0).plus(offsetLocalTimeZone - summitTimeZoneOffset);
        DateTime endDate = new DateTime(view.getSelectedDate()).withTime(23, 59, 59, 999).plus(offsetLocalTimeZone - summitTimeZoneOffset);

        dayEvents = getScheduleEvents(startDate, endDate, interactor);
        view.setEvents(dayEvents);
        view.reloadSchedule();
    }

    protected List<ScheduleItemDTO> getScheduleEvents(DateTime startDate, DateTime endDate, I interactor) {
        List<ScheduleItemDTO> events = interactor.getScheduleEvents(startDate.toDate(), endDate.toDate(), null, null, null, null, null);
        return events;
    }

    public void toggleScheduleStatus(IScheduleItemView scheduleItemView, int position) {
        ScheduleItemDTO scheduleItemDTO = dayEvents.get(position);
        toggleScheduledStatusForEvent(scheduleItemDTO, scheduleItemView, interactor);
    }

    @Override
    public void showEventDetail(int position) {
        ScheduleItemDTO scheduleItemDTO = dayEvents.get(position);
        wireframe.showEventDetail(view.getActivity());
    }
}