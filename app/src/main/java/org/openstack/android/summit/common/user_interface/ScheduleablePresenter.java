package org.openstack.android.summit.common.user_interface;

import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.business_logic.IScheduleableInteractor;
import java.util.HashMap;
import io.reactivex.Observable;

/**
 * Created by Claudio Redi on 1/5/2016.
 */
public class ScheduleablePresenter implements IScheduleablePresenter {

    private final static HashMap<String, String> ongoingOperations = new HashMap<>();
    private final static Object lock                               = new Object();

    private static boolean hasOp(Integer eventId, String op){
        synchronized (lock){
            return ongoingOperations.containsKey(eventId.toString()+"_"+op);
        }
    }

    private static void addOp(Integer eventId, String op){
        synchronized (lock){
            ongoingOperations.put(eventId.toString()+"_"+op, eventId.toString()+"_"+op);
        }
    }

    private static void removeOp(Integer eventId, String op){
        synchronized (lock){
            ongoingOperations.remove(eventId.toString()+"_"+op);
        }
    }

    @Override
    public Observable<Boolean> toggleScheduledStatusForEvent
    (
        ScheduleItemDTO scheduleItemDTO,
        IScheduleableItem scheduleableView,
        IScheduleableInteractor interactor
    )
    {

        Boolean isScheduled = interactor.isEventScheduledByLoggedMember(scheduleItemDTO.getId());

        return isScheduled ?
                removeEventFromSchedule(scheduleItemDTO, scheduleableView, interactor) :
                addEventToSchedule(scheduleItemDTO, scheduleableView, interactor);
    }

    @Override
    public Observable<Boolean> toggleFavoriteStatusForEvent
    (
        ScheduleItemDTO scheduleItemDTO,
        IScheduleableItem favoriteView,
        IScheduleableInteractor interactor
    )
    {
        Boolean isFavorite = interactor.isEventFavoriteByLoggedMember(scheduleItemDTO.getId());
        return isFavorite ?
                removeEventFromFavorites(scheduleItemDTO, favoriteView, interactor) :
                addEvent2Favorites(scheduleItemDTO, favoriteView, interactor);
    }

    private Observable<Boolean> removeEventFromFavorites(ScheduleItemDTO scheduleItemDTO,
                                          IFavoriteView scheduleableView,
                                          IScheduleableInteractor interactor){
        // update view
        if(hasOp(scheduleItemDTO.getId(), "FAV_DEL")) return Observable.just(false);
        addOp(scheduleItemDTO.getId(), "FAV_DEL");
        scheduleableView.setFavorite(false);

        return interactor
                .removeEventFromMemberFavorites(scheduleItemDTO.getId())
                .doOnNext((res) ->  removeOp(scheduleItemDTO.getId(), "FAV_DEL"))
                .doOnError((res) -> { removeOp(scheduleItemDTO.getId(), "FAV_DEL"); scheduleableView.setFavorite(true);});
    }

    private Observable<Boolean> addEvent2Favorites(ScheduleItemDTO scheduleItemDTO,
                                          IFavoriteView scheduleableView,
                                          IScheduleableInteractor interactor){
        // update view
        if(hasOp(scheduleItemDTO.getId(), "FAV_ADD")) return Observable.just(false);
        addOp(scheduleItemDTO.getId(), "FAV_ADD");
        scheduleableView.setFavorite(true);

        return interactor
                .addEventToMyFavorites(scheduleItemDTO.getId())
                .doOnNext((res) ->  removeOp(scheduleItemDTO.getId(), "FAV_ADD"))
                .doOnError((res) -> { removeOp(scheduleItemDTO.getId(), "FAV_ADD");});
    }


    private Observable<Boolean> removeEventFromSchedule(ScheduleItemDTO scheduleItemDTO, final IScheduleableView scheduleableView, IScheduleableInteractor interactor)
    {
        if(hasOp(scheduleItemDTO.getId(), "GOING_DEL")) return Observable.just(false);
        addOp(scheduleItemDTO.getId(), "GOING_DEL");
        scheduleableView.setScheduled(false);

        return interactor
                .removeEventFromLoggedInMemberSchedule(scheduleItemDTO.getId())
                .doOnNext((res) ->  removeOp(scheduleItemDTO.getId(), "GOING_DEL"))
                .doOnError((res) -> { removeOp(scheduleItemDTO.getId(), "GOING_DEL");});
    }

    private Observable<Boolean> addEventToSchedule
    (
        ScheduleItemDTO scheduleItemDTO,
        IScheduleableView scheduleableView,
        IScheduleableInteractor interactor
    )
    {
        if(hasOp(scheduleItemDTO.getId(), "GOING_ADD")) return Observable.just(false);
        addOp(scheduleItemDTO.getId(), "GOING_ADD");
        scheduleableView.setScheduled(true);

        return interactor
                .addEventToLoggedInMemberSchedule(scheduleItemDTO.getId())
                .doOnNext((res) ->  removeOp(scheduleItemDTO.getId(), "GOING_ADD"))
                .doOnError((res) -> { removeOp(scheduleItemDTO.getId(), "GOING_ADD");});
    }
}
