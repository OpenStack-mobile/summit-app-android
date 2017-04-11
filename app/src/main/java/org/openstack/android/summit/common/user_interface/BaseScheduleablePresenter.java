package org.openstack.android.summit.common.user_interface;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.IScheduleWireframe;
import org.openstack.android.summit.common.business_logic.IScheduleableInteractor;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by smarcet on 3/2/17.
 */

public abstract class BaseScheduleablePresenter<V extends IBaseView, I extends IScheduleableInteractor, W extends IScheduleWireframe>
        extends BasePresenter<V, I, W> {

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

        if(!this.interactor.isMemberLoggedIn()){
            buildLoginModal().show();
            return;
        }

        if(!this.interactor.isMemberLoggedInAndConfirmedAttendee()){
            buildAttendeeModal().show();
            return;
        }

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
                                Crashlytics.logException(ex);
                            }
                            if(view != null) AlertsBuilder.buildGenericError(view.getFragmentActivity()).show();
                        }
                );
    }

    protected void _toggleFavoriteStatus(IScheduleableItem scheduleItemView, int position) {
        ScheduleItemDTO scheduleItemDTO = getCurrentItem(position);
        if(scheduleItemDTO == null) return;

        if(!this.interactor.isMemberLoggedIn()){
            // Use the Builder class for convenient dialog construction
            buildLoginModal().show();
            return;
        }

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
                                        Crashlytics.logException(ex);
                                    }
                                    if(view != null) AlertsBuilder.buildGenericError(view.getFragmentActivity()).show();
                                }
                        );
    }

    protected void _toggleRSVPStatus(IScheduleableItem scheduleItemView, int position) {

        ScheduleItemDTO scheduleItemDTO = getCurrentItem(position);
        if(scheduleItemDTO == null) return;

        if(!this.interactor.isMemberLoggedIn()){
            buildLoginModal().show();
            return;
        }

        if(!this.interactor.isMemberLoggedInAndConfirmedAttendee()){
            buildAttendeeModal().show();
            return;
        }

        boolean formerState = scheduleItemView.getScheduled();

        if(scheduleItemView.isExternalRSVP()){
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
                                    Crashlytics.logException(ex);
                                    Log.d(Constants.LOG_TAG, ex.getMessage());
                                }
                                if(view != null) AlertsBuilder.buildGenericError(view.getFragmentActivity()).show();
                            }
                    );
        }
        else{
            if(formerState){
                scheduleablePresenter.deleteRSVP(scheduleItemDTO, scheduleItemView, interactor)
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
                                        Crashlytics.logException(ex);
                                    }
                                    if(view != null) AlertsBuilder.buildGenericError(view.getFragmentActivity()).show();
                                }
                        );
            }
            else{
                wireframe.presentEventRsvpView(scheduleItemView.getRSVPLink(), view);
            }
        }
    }

    protected void _shareEvent(IScheduleableItem scheduleItemView, int position){
        ScheduleItemDTO scheduleItemDTO = getCurrentItem(position);
        if(scheduleItemDTO == null) return;
        view.startActivity(Intent.createChooser(ShareIntentBuilder.build(scheduleItemDTO.getSocialSummary(), scheduleItemDTO.getEventUrl()), view.getResources().getString(R.string.action_share_event)));
    }

    protected void _rateEvent(IScheduleableItem scheduleItemView, int position){
        ScheduleItemDTO scheduleItemDTO = getCurrentItem(position);
        if(scheduleItemDTO == null) return;

        if(!this.interactor.isMemberLoggedIn()){
            buildLoginModal().show();
            return;
        }

        if(!scheduleItemDTO.isStarted()){
            AlertsBuilder.buildValidationError(view.getFragmentActivity(), view.getResources().getString(R.string.feedback_validation_error_event_not_started)).show();
            return;
        }

        wireframe.showFeedbackEditView(scheduleItemDTO.getId(), scheduleItemDTO.getName(), 0, view);

    }


    protected AlertDialog buildLoginModal(){
        onBeforeLoginModal();
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getFragmentActivity());

        builder.setTitle(R.string.alert_login_required_title)
                .setMessage(R.string.alert_login_required_body)
                .setPositiveButton(R.string.alert_login_required_ok, (dialog, id) -> {
                    Intent intent = new Intent(Constants.DO_EXTERNAL_LOG_IN_EVENT);
                    LocalBroadcastManager
                            .getInstance(OpenStackSummitApplication.context)
                            .sendBroadcast(intent);

                    dialog.dismiss();
                })
                .setNegativeButton(R.string.alert_login_required_cancel, (dialog, id) -> {
                    dialog.dismiss();
                });
        return builder.create();
    }

    protected AlertDialog buildAttendeeModal(){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getFragmentActivity());
        onBeforeAttendeeModal();
        builder.setTitle(R.string.alert_attendee_required_title)
                .setMessage(R.string.alert_attendee_required_body)
                .setPositiveButton(R.string.alert_attendee_required_ok, (dialog, id) -> {
                    Intent intent = new Intent(Constants.DO_EXTERNAL_REDEEM_ORDER_EVENT);
                    LocalBroadcastManager
                            .getInstance(OpenStackSummitApplication.context)
                            .sendBroadcast(intent);

                    dialog.dismiss();
                })
                .setNegativeButton(R.string.alert_attendee_required_cancel, (dialog, id) -> {

                    dialog.dismiss();
                });
        return builder.create();
    }

    protected void onBeforeLoginModal(){

    }

    protected void onBeforeAttendeeModal(){

    }
}
