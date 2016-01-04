package org.openstack.android.summit.common.user_interface;

import android.os.Bundle;

import org.joda.time.DateTime;
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
public class SchedulePresenter<V extends ScheduleFragment, I extends IScheduleInteractor, W extends IScheduleWireframe> implements ISchedulePresenter<V, I , W> {
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
        InteractorAsyncOperationListener<SummitDTO> summitDTOIInteractorOperationListener = new InteractorAsyncOperationListener<SummitDTO>() {
            @Override
            public void onSuceedWithData(SummitDTO data) {
                summitTimeZoneOffset = TimeZone.getTimeZone(data.getTimeZone()).getOffset(new Date().getTime());
                DateTime startDate = new DateTime(data.getStartDate()).plus(summitTimeZoneOffset).withTime(0, 0, 0, 0);
                DateTime endDate = new DateTime(data.getEndDate()).plus(summitTimeZoneOffset).plusDays(1);

                view.setStartAndEndDateWithParts(
                        startDate.getYear(),
                        startDate.getMonthOfYear(),
                        startDate.getDayOfMonth(),
                        endDate.getYear(),
                        endDate.getMonthOfYear(),
                        endDate.getDayOfMonth()
                );
                reloadSchedule();
            }

            @Override
            public void onError(String message) {
                super.onError(message);
            }
        };

        interactor.getActiveSummit(summitDTOIInteractorOperationListener);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void buildItem(IScheduleItemView scheduleItemView, int position) {
        ScheduleItemDTO scheduleItemDTO = dayEvents.get(position);
        scheduleItemView.setName(scheduleItemDTO.getName());
        scheduleItemView.setTime(scheduleItemDTO.getTime());
        scheduleItemView.setSponsors(scheduleItemDTO.getSponsors());
        scheduleItemView.setEventType(scheduleItemDTO.getEventType().toUpperCase());
        scheduleItemView.setTrack(scheduleItemDTO.getTrack());
        String summitTypeColor = scheduleItemDTO.getSummitTypeColor() != "" ? scheduleItemDTO.getSummitTypeColor() : "#4A4A4A";
        scheduleItemView.setSummitTypeColor(summitTypeColor);
    }

    public void reloadSchedule() {
        selectedDate = view.getSelectedDate();

        int offsetLocalTimeZone  = TimeZone.getDefault().getOffset(new Date().getTime());

        DateTime startDate = new DateTime(view.getSelectedDate()).plus(offsetLocalTimeZone - summitTimeZoneOffset).withTime(0, 0, 0, 0);
        DateTime endDate = new DateTime(view.getSelectedDate()).withTime(23, 59, 59, 999).plus(offsetLocalTimeZone - summitTimeZoneOffset);

        dayEvents = getScheduledEvents(startDate, endDate, interactor);
        view.setEvents(dayEvents);
        view.reloadSchedule();
    }

    private List<ScheduleItemDTO> getScheduledEvents(DateTime startDate, DateTime endDate, I interactor) {
        List<ScheduleItemDTO> events = interactor.getScheduleEvents(startDate.toDate(), endDate.toDate(), null, null, null, null, null);
        return events;
    }

    public void toggleScheduleStatus(IScheduleItemView scheduleItemView, int position) {
        ScheduleItemDTO scheduleItemDTO = dayEvents.get(position);
        toggleScheduledStatusForEvent(scheduleItemDTO, scheduleItemView, interactor);
    }

    public void toggleScheduledStatusForEvent(ScheduleItemDTO scheduleItemDTO, IScheduleableView scheduleableView, IScheduleInteractor interactor) {
        Boolean isScheduled = interactor.isEventScheduledByLoggedMember(scheduleItemDTO.getId());

        if (isScheduled) {
            removeEventFromSchedule(scheduleItemDTO, scheduleableView, interactor);
        }
        else {
            addEventToSchedule(scheduleItemDTO, scheduleableView, interactor);
        }

    }

    private void removeEventFromSchedule(ScheduleItemDTO scheduleItemDTO, IScheduleableView scheduleableView, IScheduleInteractor interactor) {
        scheduleableView.setScheduled(false);
        final IScheduleableView finalScheduleable = scheduleableView;
        InteractorAsyncOperationListener<Void> interactorOperationListener = new InteractorAsyncOperationListener<Void>() {
            @Override
            public void onSuceedWithData(Void data) {

            }

            @Override
            public void onError(String message) {
                finalScheduleable.setScheduled(!finalScheduleable.getScheduled());
            }
        };

        interactor.removeEventToLoggedInMemberSchedule(scheduleItemDTO.getId(), interactorOperationListener);
    }

    private void addEventToSchedule(ScheduleItemDTO scheduleItemDTO, IScheduleableView scheduleableView, IScheduleInteractor interactor) {
        scheduleableView.setScheduled(true);
        final IScheduleableView finalScheduleable = scheduleableView;
        InteractorAsyncOperationListener<Void> interactorOperationListener = new InteractorAsyncOperationListener<Void>() {
            @Override
            public void onSuceedWithData(Void data) {

            }

            @Override
            public void onError(String message) {
                finalScheduleable.setScheduled(!finalScheduleable.getScheduled());
            }
        };

        interactor.addEventToLoggedInMemberSchedule(scheduleItemDTO.getId(), interactorOperationListener);
    }


    /*
    public func toggleScheduledStatus(index: Int, cell: IScheduleTableViewCell) {
        let event = dayEvents[index]
        toggleScheduledStatusForEvent(event, scheduleableView: cell, interactor: internalInteractor) { error in
            if (error != nil) {
                self.internalViewController.showErrorMessage(error!)
            }
        }
    }

    func toggleScheduledStatusForEvent(event: ScheduleItemDTO, scheduleableView: IScheduleableView, interactor: IScheduleableInteractor, completionBlock: ((NSError?) -> Void)?) {
        let isScheduled = interactor.isEventScheduledByLoggedMember(event.id)
        if (isScheduled) {
            removeEventFromSchedule(event, scheduleableView: scheduleableView, interactor: interactor, completionBlock: completionBlock)
        }
        else {
            addEventToSchedule(event, scheduleableView: scheduleableView, interactor: interactor, completionBlock: completionBlock)
        }
    }

    func addEventToSchedule(event: ScheduleItemDTO, scheduleableView: IScheduleableView, interactor: IScheduleableInteractor, completionBlock: ((NSError?) -> Void)?) {
        scheduleableView.scheduled = true

        interactor.addEventToLoggedInMemberSchedule(event.id) { error in
            dispatch_async(dispatch_get_main_queue(),{
            if (error != nil) {
                scheduleableView.scheduled = !scheduleableView.scheduled
            }

            if (completionBlock != nil) {
                completionBlock!(error)
            }
            })
        }
    }

    func removeEventFromSchedule(event: ScheduleItemDTO, scheduleableView: IScheduleableView, interactor: IScheduleableInteractor, completionBlock: ((NSError?) -> Void)?) {
        scheduleableView.scheduled = false

        interactor.removeEventFromLoggedInMemberSchedule(event.id) { error in
            dispatch_async(dispatch_get_main_queue(),{
            if (error != nil) {
                scheduleableView.scheduled = !scheduleableView.scheduled
            }
            })

            if (completionBlock != nil) {
                completionBlock!(error)
            }
        }
    } */
}