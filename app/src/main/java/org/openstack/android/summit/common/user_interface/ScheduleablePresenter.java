package org.openstack.android.summit.common.user_interface;

import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.business_logic.IInteractorAsyncOperationListener;
import org.openstack.android.summit.common.business_logic.IScheduleableInteractor;
import org.openstack.android.summit.common.business_logic.InteractorAsyncOperationListener;

/**
 * Created by Claudio Redi on 1/5/2016.
 */
public class ScheduleablePresenter implements IScheduleablePresenter {
    private boolean hasOngoingOperation;

    @Override
    public void toggleScheduledStatusForEvent(ScheduleItemDTO scheduleItemDTO, IScheduleableView scheduleableView, IScheduleableInteractor interactor, IInteractorAsyncOperationListener<Void> interactorOperationListener) {
        if (hasOngoingOperation()) {
            return;
        }

        hasOngoingOperation = true;
        Boolean isScheduled = interactor.isEventScheduledByLoggedMember(scheduleItemDTO.getId());

        if (isScheduled) {
            removeEventFromSchedule(scheduleItemDTO, scheduleableView, interactor, interactorOperationListener);
        }
        else {
            addEventToSchedule(scheduleItemDTO, scheduleableView, interactor, interactorOperationListener);
        }
    }

    private void removeEventFromSchedule(ScheduleItemDTO scheduleItemDTO, final IScheduleableView scheduleableView, IScheduleableInteractor interactor, final IInteractorAsyncOperationListener<Void> interactorOperationListener) {
        scheduleableView.setScheduled(false);
        InteractorAsyncOperationListener<Void> internalInteractorOperationListener = new InteractorAsyncOperationListener<Void>() {
            @Override
            public void onSucceed() {
                interactorOperationListener.onSucceed();
                hasOngoingOperation = false;
            }

            @Override
            public void onError(String message) {
                scheduleableView.setScheduled(!scheduleableView.getScheduled());
                interactorOperationListener.onError(message);
                hasOngoingOperation = false;
            }
        };

        interactor.removeEventFromLoggedInMemberSchedule(scheduleItemDTO.getId(), internalInteractorOperationListener);
    }

    private void addEventToSchedule(ScheduleItemDTO scheduleItemDTO, final IScheduleableView scheduleableView, IScheduleableInteractor interactor, final IInteractorAsyncOperationListener<Void> interactorOperationListener) {
        scheduleableView.setScheduled(true);
        InteractorAsyncOperationListener<Void> internalInteractorOperationListener = new InteractorAsyncOperationListener<Void>() {
            @Override
            public void onSucceed() {
                interactorOperationListener.onSucceed();
                hasOngoingOperation = false;
            }

            @Override
            public void onError(String message) {
                scheduleableView.setScheduled(!scheduleableView.getScheduled());
                interactorOperationListener.onError(message);
                hasOngoingOperation = false;
            }
        };

        interactor.addEventToLoggedInMemberSchedule(scheduleItemDTO.getId(), internalInteractorOperationListener);
    }

    public boolean hasOngoingOperation() {
        return hasOngoingOperation;
    }
}
