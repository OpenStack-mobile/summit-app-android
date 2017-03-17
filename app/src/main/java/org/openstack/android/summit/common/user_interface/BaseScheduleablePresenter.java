package org.openstack.android.summit.common.user_interface;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.IScheduleWireframe;
import org.openstack.android.summit.common.business_logic.IScheduleableInteractor;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by smarcet on 3/2/17.
 */

public abstract class BaseScheduleablePresenter<V extends IBaseView, I extends IScheduleableInteractor, W extends IScheduleWireframe> extends BasePresenter<V, I, W> {

    public interface ToggleScheduleStatusListener{
        void toggle(int position, boolean formerState, IScheduleableItem viewItem);
    }

    public interface ToggleFavoriteStatusListener{
        void toggle(int position, boolean formerState, IScheduleableItem viewItem);
    }

    protected IScheduleablePresenter scheduleablePresenter;
    protected ToggleScheduleStatusListener toggleScheduleStatusListener;
    protected ToggleFavoriteStatusListener toggleFavoriteStatusListener;

    public BaseScheduleablePresenter(I interactor, W wireframe, IScheduleablePresenter scheduleablePresenter) {
        super(interactor, wireframe);
        this.scheduleablePresenter = scheduleablePresenter;
    }

    protected abstract ScheduleItemDTO getCurrentItem(int position);

    protected void _toggleScheduleStatus(IScheduleableItem scheduleItemView, int position) {

        ScheduleItemDTO scheduleItemDTO = getCurrentItem(position);
        if(scheduleItemDTO == null) return;
        boolean formerState = scheduleItemView.getScheduled();

        if(toggleScheduleStatusListener != null){
            toggleScheduleStatusListener.toggle(position, formerState, scheduleItemView);
        }

        scheduleablePresenter
                .toggleScheduledStatusForEvent(scheduleItemDTO, scheduleItemView, interactor)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (res) -> {
                            if(view != null && view.getApplicationContext() != null)
                            Toast.makeText(view.getApplicationContext(), formerState ?
                                            view.getResources().getString(R.string.removed_from_going):
                                            view.getResources().getString(R.string.added_2_going),
                                    Toast.LENGTH_SHORT).show();
                        },
                        (ex) -> {
                            scheduleItemView.setScheduled(formerState);
                            if(ex != null) {
                                Log.d(Constants.LOG_TAG, ex.getMessage());
                                if(view != null)
                                    view.showErrorMessage(ex.getMessage());
                                return;
                            }
                            if(view != null)
                                view.showErrorMessage("Server Error");
                        }
                );
    }

    protected void _toggleFavoriteStatus(IScheduleableItem scheduleItemView, int position) {
        ScheduleItemDTO scheduleItemDTO = getCurrentItem(position);
        if(scheduleItemDTO == null) return;
        // get former state
        boolean formerState = scheduleItemView.getFavorite();

        if(toggleFavoriteStatusListener != null) {
            toggleFavoriteStatusListener.toggle(position, formerState, scheduleItemView);
        }

        scheduleablePresenter
                .toggleFavoriteStatusForEvent(scheduleItemDTO, scheduleItemView, interactor)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe
                        (
                                (res) -> {
                                    if(view != null && view.getApplicationContext() != null)
                                    Toast.makeText(view.getApplicationContext(), formerState ?
                                                    view.getResources().getString(R.string.removed_from_favorites):
                                                    view.getResources().getString(R.string.added_2_favorites),
                                            Toast.LENGTH_SHORT).show();
                                },
                                (ex) -> {
                                    scheduleItemView.setFavorite(formerState);
                                    if(ex != null) {
                                        Log.d(Constants.LOG_TAG, ex.getMessage());
                                        if(view != null)
                                            view.showErrorMessage(ex.getMessage());
                                        return;
                                    }
                                    if(view != null)
                                        view.showErrorMessage("Server Error");
                                }
                        );
    }

    protected void _toggleRSVPStatus(IScheduleableItem scheduleItemView, int position) {

        ScheduleItemDTO scheduleItemDTO = getCurrentItem(position);
        if(scheduleItemDTO == null) return;
        boolean formerState = scheduleItemView.getScheduled();
        if(!formerState){
            if(!scheduleItemView.isExternalRSVP()){
                wireframe.presentEventRsvpView(scheduleItemView.getRSVPLink(), view);
                return;
            }
            // its external, add to schedule and then show view

            scheduleablePresenter
                    .toggleScheduledStatusForEvent(scheduleItemDTO, scheduleItemView, interactor)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            (res) -> {
                                if(view != null && view.getApplicationContext() != null)
                                Toast.makeText(view.getApplicationContext(), formerState ?
                                                view.getResources().getString(R.string.removed_from_going):
                                                view.getResources().getString(R.string.added_2_going),
                                        Toast.LENGTH_SHORT).show();

                                wireframe.presentEventRsvpView(scheduleItemView.getRSVPLink(), view);
                            },
                            (ex) -> {
                                scheduleItemView.setScheduled(formerState);
                                if(ex != null) {
                                    Log.d(Constants.LOG_TAG, ex.getMessage());
                                    if(view != null)
                                        view.showErrorMessage(ex.getMessage());
                                    return;
                                }
                                if(view != null)
                                    view.showErrorMessage("Server Error");
                            }
                    );
        }
    }

    protected void _shareEvent(IScheduleableItem scheduleItemView, int position){
        ScheduleItemDTO scheduleItemDTO = getCurrentItem(position);
        if(scheduleItemDTO == null) return;
        view.startActivity(Intent.createChooser(ShareIntentBuilder.build(scheduleItemDTO.getSocialSummary(), scheduleItemDTO.getEventUrl()), view.getResources().getString(R.string.action_share_event)));
    }
}
