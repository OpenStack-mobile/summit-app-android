package org.openstack.android.summit.common.user_interface;

import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.business_logic.IInteractorAsyncOperationListener;
import org.openstack.android.summit.common.business_logic.IScheduleableInteractor;
import org.openstack.android.summit.common.business_logic.InteractorAsyncOperationListener;

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
    public void toggleScheduledStatusForEvent(ScheduleItemDTO scheduleItemDTO, IScheduleableView scheduleableView, IScheduleableInteractor interactor, IInteractorAsyncOperationListener<Void> interactorOperationListener) {

        Boolean isScheduled = interactor.isEventScheduledByLoggedMember(scheduleItemDTO.getId());

        if (isScheduled) {
            removeEventFromSchedule(scheduleItemDTO, scheduleableView, interactor, interactorOperationListener);
        }
        else {
            addEventToSchedule(scheduleItemDTO, scheduleableView, interactor, interactorOperationListener);
        }
    }

    @Override
    public Observable<Boolean> toggleFavoriteStatusForEvent
    (
        ScheduleItemDTO scheduleItemDTO,
        IFavoriteView favoriteView,
        IScheduleableInteractor interactor
    )
    {
        Boolean isFavorite = interactor.isEventFavoriteByLoggedMember(scheduleItemDTO.getId());
        return isFavorite ?
                removeEventFromFavorites(scheduleItemDTO, favoriteView, interactor) :
                addEven2Favorites(scheduleItemDTO, favoriteView, interactor);
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

    private Observable<Boolean> addEven2Favorites(ScheduleItemDTO scheduleItemDTO,
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


    private void removeEventFromSchedule(ScheduleItemDTO scheduleItemDTO, final IScheduleableView scheduleableView, IScheduleableInteractor interactor, final IInteractorAsyncOperationListener<Void> interactorOperationListener) {

        // update view
        if(hasOp(scheduleItemDTO.getId(), "GOING_DEL")) return;
        addOp(scheduleItemDTO.getId(), "GOING_DEL");
        scheduleableView.setScheduled(false);
        InteractorAsyncOperationListener<Void> internalInteractorOperationListener = new InteractorAsyncOperationListener<Void>() {
            @Override
            public void onSucceed() {

                removeOp(scheduleItemDTO.getId(), "GOING_DEL");
                interactorOperationListener.onSucceed();
            }

            @Override
            public void onError(String message) {
                removeOp(scheduleItemDTO.getId(), "GOING_DEL");
                scheduleableView.setScheduled(!scheduleableView.getScheduled());
                interactorOperationListener.onError(message);
            }
        };

        interactor.removeEventFromLoggedInMemberSchedule(scheduleItemDTO.getId(), internalInteractorOperationListener);
    }

    private void addEventToSchedule(ScheduleItemDTO scheduleItemDTO, final IScheduleableView scheduleableView, IScheduleableInteractor interactor, final IInteractorAsyncOperationListener<Void> interactorOperationListener) {
        if(hasOp(scheduleItemDTO.getId(), "GOING_ADD")) return;
        addOp(scheduleItemDTO.getId(), "GOING_ADD");
        scheduleableView.setScheduled(true);
        InteractorAsyncOperationListener<Void> internalInteractorOperationListener = new InteractorAsyncOperationListener<Void>() {
            @Override
            public void onSucceed() {
                removeOp(scheduleItemDTO.getId(), "GOING_ADD");
                interactorOperationListener.onSucceed();
            }

            @Override
            public void onError(String message) {
                removeOp(scheduleItemDTO.getId(), "GOING_ADD");
                scheduleableView.setScheduled(!scheduleableView.getScheduled());
                interactorOperationListener.onError(message);
            }
        };

        interactor.addEventToLoggedInMemberSchedule(scheduleItemDTO.getId(), internalInteractorOperationListener);
    }
}
