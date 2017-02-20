package org.openstack.android.summit.modules.personal_schedule.user_interface;

import android.util.Log;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.business_logic.IInteractorAsyncOperationListener;
import org.openstack.android.summit.common.business_logic.InteractorAsyncOperationListener;
import org.openstack.android.summit.common.user_interface.IScheduleItemView;
import org.openstack.android.summit.common.user_interface.IScheduleItemViewBuilder;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.SchedulePresenter;
import org.openstack.android.summit.modules.personal_schedule.IPersonalScheduleWireframe;
import org.openstack.android.summit.modules.personal_schedule.business_logic.IPersonalScheduleInteractor;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public class PersonalSchedulePresenter extends SchedulePresenter<IPersonalScheduleView, IPersonalScheduleInteractor, IPersonalScheduleWireframe> implements IPersonalSchedulePresenter {

    public PersonalSchedulePresenter(IPersonalScheduleInteractor interactor, IPersonalScheduleWireframe wireframe, IScheduleablePresenter scheduleablePresenter, IScheduleItemViewBuilder scheduleItemViewBuilder, IScheduleFilter scheduleFilter) {
        super(interactor, wireframe, scheduleablePresenter, scheduleItemViewBuilder, scheduleFilter);
    }

    @Override
    protected List<ScheduleItemDTO> getScheduleEvents(DateTime startDate, DateTime endDate, IPersonalScheduleInteractor interactor) {
        List<ScheduleItemDTO> events = null;
        if (interactor.isMemberLoggedInAndConfirmedAttendee()) {
            events = interactor.getCurrentMemberScheduledEvents(startDate.toDate(), endDate.toDate());
        }
        else {
            events = new ArrayList<>();
        }
        return events;
    }

    @Override
    protected List<DateTime> getDatesWithoutEvents(DateTime startDate, DateTime endDate) {
        return interactor.isMemberLoggedInAndConfirmedAttendee()?
                interactor.getCurrentMemberScheduleDatesWithoutEvents(startDate, endDate):
                new ArrayList<>();
    }

    @Override
    public void toggleScheduleStatus(IScheduleItemView scheduleItemView, final int position) {
        if(dayEvents.size() - 1 < position ) return;
        ScheduleItemDTO scheduleItemDTO = dayEvents.get(position);
        boolean formerState = scheduleItemView.getScheduled();

        if(formerState && !scheduleItemView.getFavorite()) {
            removeItem(position);
        }

        IInteractorAsyncOperationListener<Void> interactorAsyncOperationListener = new InteractorAsyncOperationListener<Void>() {
            @Override
            public void onError(String message) {

                view.showErrorMessage(message);
            }
            @Override
            public void onSucceed(){
                Toast.makeText(view.getApplicationContext(), formerState ?
                                view.getResources().getString(R.string.removed_from_going):
                                view.getResources().getString(R.string.added_2_going),
                        Toast.LENGTH_SHORT).show();
            }
        };
        scheduleablePresenter.toggleScheduledStatusForEvent(scheduleItemDTO, scheduleItemView, interactor, interactorAsyncOperationListener);
    }

    @Override
    public void removeItem(int position){
        super.removeItem(position);
        view.removeItem(position);
    }

    @Override
    public void toggleFavoriteStatus(IScheduleItemView scheduleItemView, int position) {
        if(dayEvents.size() - 1 < position ) return;

        ScheduleItemDTO scheduleItemDTO = dayEvents.get(position);
        // get former state
        boolean formerState             = scheduleItemView.getFavorite();

        if(formerState && !scheduleItemView.getScheduled()) {
            removeItem(position);
        }

        scheduleablePresenter
                .toggleFavoriteStatusForEvent(scheduleItemDTO, scheduleItemView, interactor)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe
                        (
                                (res) -> {
                                    Toast.makeText(view.getApplicationContext(), formerState ?
                                                    view.getResources().getString(R.string.removed_from_favorites):
                                                    view.getResources().getString(R.string.added_2_favorites),
                                            Toast.LENGTH_SHORT).show();
                                },
                                (ex) -> {
                                    scheduleItemView.setFavorite(formerState);
                                    if(ex != null) {
                                        Log.d(Constants.LOG_TAG, ex.getMessage());
                                        view.showErrorMessage(ex.getMessage());
                                        return;
                                    }
                                    view.showErrorMessage("Server Error");
                                }

                        );
    }
}
