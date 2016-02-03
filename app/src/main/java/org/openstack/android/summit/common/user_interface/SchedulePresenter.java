package org.openstack.android.summit.common.user_interface;

import android.os.Bundle;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.IScheduleWireframe;
import org.openstack.android.summit.common.business_logic.IInteractorAsyncOperationListener;
import org.openstack.android.summit.common.business_logic.IScheduleInteractor;
import org.openstack.android.summit.common.business_logic.InteractorAsyncOperationListener;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Claudio Redi on 12/28/2015.
 */
public abstract class SchedulePresenter<V extends IScheduleView, I extends IScheduleInteractor, W extends IScheduleWireframe> extends BasePresenter<V, I, W> implements ISchedulePresenter<V> {
    List<ScheduleItemDTO> dayEvents;
    Date selectedDate;
    int summitTimeZoneOffset;
    protected InteractorAsyncOperationListener<ScheduleItemDTO> scheduleItemDTOIInteractorOperationListener;
    private IScheduleItemViewBuilder scheduleItemViewBuilder;
    private IScheduleablePresenter scheduleablePresenter;

    public SchedulePresenter(I interactor, W wireframe, IScheduleablePresenter scheduleablePresenter, IScheduleItemViewBuilder scheduleItemViewBuilder) {
        super(interactor, wireframe);
        this.scheduleablePresenter = scheduleablePresenter;
        this.scheduleItemViewBuilder = scheduleItemViewBuilder;
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
        initialize();
    }

    private void initialize() {
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
        scheduleItemViewBuilder.build(
                scheduleItemView,
                scheduleItemDTO,
                interactor.isMemberLoggedIn(),
                interactor.isEventScheduledByLoggedMember(scheduleItemDTO.getId()),
                false
        );
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