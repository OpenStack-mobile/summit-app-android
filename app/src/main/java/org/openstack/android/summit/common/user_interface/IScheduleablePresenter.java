package org.openstack.android.summit.common.user_interface;

import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.business_logic.IInteractorAsyncOperationListener;
import org.openstack.android.summit.common.business_logic.IScheduleableInteractor;

import io.reactivex.Observable;

/**
 * Created by Claudio Redi on 1/14/2016.
 */
public interface IScheduleablePresenter {

    void toggleScheduledStatusForEvent(ScheduleItemDTO scheduleItemDTO, IScheduleableView scheduleableView, IScheduleableInteractor interactor, IInteractorAsyncOperationListener<Void> interactorOperationListener);

    Observable<Boolean> toggleFavoriteStatusForEvent(ScheduleItemDTO scheduleItemDTO, IFavoriteView favoriteView, IScheduleableInteractor interactor);

}
