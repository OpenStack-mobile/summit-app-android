package org.openstack.android.summit.common.user_interface;

import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.business_logic.IInteractorAsyncOperationListener;
import org.openstack.android.summit.common.business_logic.IScheduleableInteractor;
import org.openstack.android.summit.common.business_logic.InteractorAsyncOperationListener;

/**
 * Created by Claudio Redi on 1/14/2016.
 */
public interface IScheduleablePresenter {
    void toggleScheduledStatusForEvent(ScheduleItemDTO scheduleItemDTO, IScheduleableView scheduleableView, IScheduleableInteractor interactor, IInteractorAsyncOperationListener<Void> interactorOperationListener);
}
