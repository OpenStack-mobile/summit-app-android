package org.openstack.android.summit.common.user_interface;

import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.IScheduleWireframe;
import org.openstack.android.summit.common.business_logic.IScheduleableInteractor;
import org.openstack.android.summit.common.network.NetworkException;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by smarcet on 3/2/17.
 */

public abstract class BaseScheduleablePresenter<V extends IBaseView, I extends IScheduleableInteractor, W extends IScheduleWireframe>
        extends BasePresenter<V, I, W> {

    protected boolean shouldShowVenues        = false;
    protected boolean isMemberLogged          = false;
    protected boolean isMemberAttendee        = false;

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

    @Override
    public void onResume() {
        super.onResume();
        shouldShowVenues        = interactor.shouldShowVenues();
        isMemberLogged          = interactor.isMemberLoggedIn();
        isMemberAttendee        = interactor.isMemberLoggedInAndConfirmedAttendee();
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
                            if(view != null) {
                                AlertDialog dialog = (ex != null && ex instanceof NetworkException) ?
                                        AlertsBuilder.buildAlert(view.getFragmentActivity(), R.string.generic_error_title, R.string.no_connectivity_message):
                                        AlertsBuilder.buildGenericError(view.getFragmentActivity());
                                if(dialog != null) dialog.show();
                            }
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
                                    if(view != null) {
                                        AlertDialog dialog = (ex != null && ex instanceof NetworkException) ?
                                                AlertsBuilder.buildAlert(view.getFragmentActivity(), R.string.generic_error_title, R.string.no_connectivity_message):
                                                AlertsBuilder.buildGenericError(view.getFragmentActivity());
                                        if(dialog != null) dialog.show();

                                    }
                                }
                        );
    }

    private void presentExternalRSVPView(ScheduleItemDTO scheduleItemDTO, IScheduleableItem scheduleItemView){
        boolean isEventScheduled = scheduleItemView.getScheduled();
        scheduleablePresenter
                .toggleScheduledStatusForEvent(scheduleItemDTO, scheduleItemView, interactor)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (res) -> {
                            if(view != null && view.getApplicationContext() != null)
                                Toast.makeText(view.getApplicationContext(), isEventScheduled ?
                                                view.getResources().getString(R.string.removed_from_going):
                                                view.getResources().getString(R.string.added_2_going),
                                        Toast.LENGTH_SHORT).show();

                            wireframe.presentEventRsvpView(scheduleItemView.getRSVPLink(), view);
                        },
                        (ex) -> {
                            scheduleItemView.setScheduled(isEventScheduled);
                            if(ex != null) {
                                Crashlytics.logException(ex);
                                Log.d(Constants.LOG_TAG, ex.getMessage());
                            }
                            if(view != null) {
                                AlertDialog dialog = AlertsBuilder.buildGenericError(view.getFragmentActivity());
                                if(dialog != null) dialog.show();
                            }
                        }
                );
    }

    private void unRSVP(ScheduleItemDTO scheduleItemDTO, IScheduleableItem scheduleItemView){
        boolean isEventScheduled = scheduleItemView.getScheduled();
        scheduleablePresenter.deleteRSVP(scheduleItemDTO, scheduleItemView, interactor)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (res) -> {
                            if(view != null && view.getApplicationContext() != null)
                                Toast.makeText(view.getApplicationContext(), isEventScheduled ?
                                                view.getResources().getString(R.string.removed_from_going):
                                                view.getResources().getString(R.string.added_2_going),
                                        Toast.LENGTH_SHORT).show();
                        },
                        (ex) -> {
                            scheduleItemView.setScheduled(isEventScheduled);
                            if(ex != null) {
                                Log.d(Constants.LOG_TAG, ex.getMessage());
                                Crashlytics.logException(ex);
                            }
                            if(view != null) {
                                AlertDialog dialog = (ex != null && ex instanceof NetworkException) ?
                                        AlertsBuilder.buildAlert(view.getFragmentActivity(), R.string.generic_error_title, R.string.no_connectivity_message):
                                        AlertsBuilder.buildGenericError(view.getFragmentActivity());
                                if(dialog != null) dialog.show();
                                scheduleItemView.setScheduled(true);
                            }
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

        if(!this.interactor.isNetworkingAvailable()){
            AlertDialog dialog = AlertsBuilder.buildAlert(view.getFragmentActivity(), R.string.generic_error_title, R.string.no_connectivity_message);
            if(dialog != null) dialog.show();
            return;
        }
        if(toggleScheduleStatusListener != null){
            toggleScheduleStatusListener.toggle(position, scheduleItemView.getScheduled(), scheduleItemView);
        }

        if(scheduleItemView.isExternalRSVP()){
            presentExternalRSVPView(scheduleItemDTO, scheduleItemView);
            return;
        }

        if(scheduleItemView.getScheduled()){
            unRSVP(scheduleItemDTO, scheduleItemView);
            return;
        }

        wireframe.presentEventRsvpView(scheduleItemView.getRSVPLink(), view);

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
            AlertDialog dialog = AlertsBuilder.buildValidationError(view.getFragmentActivity(), view.getResources().getString(R.string.feedback_validation_error_event_not_started));
            if(dialog != null) dialog.show();
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

    protected void onBeforeLoginModal(){

    }

    protected void onBeforeAttendeeModal(){

    }
}
