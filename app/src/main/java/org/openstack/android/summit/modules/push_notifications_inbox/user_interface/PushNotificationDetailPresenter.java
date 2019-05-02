package org.openstack.android.summit.modules.push_notifications_inbox.user_interface;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.PushNotificationDetailDTO;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.push_notifications_inbox.business_logic.IPushNotificationDetailInteractor;

import javax.inject.Inject;

/**
 * Created by sebastian on 8/22/2016.
 */
public class PushNotificationDetailPresenter
        extends BasePresenter<IPushNotificationDetailView, IPushNotificationDetailInteractor, IPushNotificationDetailWireframe>
        implements IPushNotificationDetailPresenter  {

    private Integer pushNotificationId;
    private PushNotificationDetailDTO pushNotification;

    @Inject
    public PushNotificationDetailPresenter(IPushNotificationDetailInteractor interactor, IPushNotificationDetailWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        pushNotificationId = (savedInstanceState != null) ?
                savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_NOTIFICATION_ID, 0):
                wireframe.getParameter(Constants.NAVIGATION_PARAMETER_NOTIFICATION_ID, Integer.class);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(pushNotificationId != null)
            outState.putInt(Constants.NAVIGATION_PARAMETER_NOTIFICATION_ID, pushNotificationId);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        pushNotification = interactor.getPushNotificationDetail(pushNotificationId != null ? pushNotificationId : 0);

        if(pushNotification == null){
            view.setSubject(view.getResources().getString(R.string.notification_not_found));
            view.hideView();
            return;
        }

        view.setSubject(pushNotification.getSubject());
        view.setBody(pushNotification.getBody());
        view.setReceived(pushNotification.getReceivedDate());
        view.setType(pushNotification.getType());
        view.showGo2EventMenuItem(pushNotification.getEventId() > 0);
    }

    @Override
    public void onRemovePushNotification() {
        interactor.deleteNotification(pushNotification);
        NotificationManager notificationManager = (NotificationManager) view.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(pushNotification.getId());
        pushNotification     = null;
        Intent deletedIntent = new Intent(Constants.PUSH_NOTIFICATION_DELETED);
        LocalBroadcastManager.getInstance(view.getApplicationContext()).sendBroadcast(deletedIntent);
        view.close();
    }

    @Override
    public void go2Event() {
        int eventId = pushNotification.getEventId();
        if(eventId > 0)
            wireframe.showEventDetail(eventId, view);
    }
}
