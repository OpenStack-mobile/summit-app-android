package org.openstack.android.summit.common.user_interface;

import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.business_logic.IScheduleableInteractor;
import io.reactivex.Observable;

/**
 * Created by Claudio Redi on 1/14/2016.
 */
public interface IScheduleablePresenter {

    Observable<Boolean> toggleScheduledStatusForEvent(ScheduleItemDTO scheduleItemDTO, IScheduleableItem scheduleableView, IScheduleableInteractor interactor);

    Observable<Boolean> deleteRSVP(ScheduleItemDTO scheduleItemDTO, IScheduleableItem scheduleableView, IScheduleableInteractor interactor);

    Observable<Boolean> toggleFavoriteStatusForEvent(ScheduleItemDTO scheduleItemDTO, IScheduleableItem favoriteView, IScheduleableInteractor interactor);

}
