package org.openstack.android.summit.common.user_interface;

import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.business_logic.IScheduleInteractor;
import org.openstack.android.summit.common.business_logic.IScheduleableInteractor;
import org.openstack.android.summit.common.business_logic.InteractorAsyncOperationListener;

/**
 * Created by Claudio Redi on 1/5/2016.
 */
public class ScheduleablePresenter implements IScheduleablePresenter {

    @Override
    public void toggleScheduledStatusForEvent(ScheduleItemDTO scheduleItemDTO, IScheduleableView scheduleableView, IScheduleableInteractor interactor) {
        Boolean isScheduled = interactor.isEventScheduledByLoggedMember(scheduleItemDTO.getId());

        if (isScheduled) {
            removeEventFromSchedule(scheduleItemDTO, scheduleableView, interactor);
        }
        else {
            addEventToSchedule(scheduleItemDTO, scheduleableView, interactor);
        }

    }

    private void removeEventFromSchedule(ScheduleItemDTO scheduleItemDTO, IScheduleableView scheduleableView, IScheduleableInteractor interactor) {
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

        interactor.removeEventFromLoggedInMemberSchedule(scheduleItemDTO.getId(), interactorOperationListener);
    }

    private void addEventToSchedule(ScheduleItemDTO scheduleItemDTO, IScheduleableView scheduleableView, IScheduleableInteractor interactor) {
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
}
