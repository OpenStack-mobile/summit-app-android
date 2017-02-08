package org.openstack.android.summit.modules.push_notifications_inbox.user_interface;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.PushNotificationListItemDTO;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.push_notifications_inbox.IPushNotificationsWireframe;
import org.openstack.android.summit.modules.push_notifications_inbox.business_logic.IPushNotificationsListInteractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastian on 8/19/2016.
 */
public class PushNotificationsListPresenter
        extends BasePresenter<IPushNotificationsListView, IPushNotificationsListInteractor, IPushNotificationsWireframe>
        implements IPushNotificationsListPresenter {

    private int page                                = 1;
    private final int OBJECTS_PER_PAGE              = 20;
    private Boolean loadedAllNotifications          = false;
    private String  term                            = "";
    private boolean resetState                      = false;
    List<PushNotificationListItemDTO> notifications = new ArrayList<PushNotificationListItemDTO>();
    private ISecurityManager securityManager;

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

                if(intent.getAction().contains(Constants.PUSH_NOTIFICATION_RECEIVED)){
                    resetState = true;
                    return;
                }

                if(intent.getAction().contains(Constants.PUSH_NOTIFICATION_DELETED)){

                    resetState = true;
                    return;
                }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if(resetState){
            resetState();
            loadData();
            resetState = false;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.PUSH_NOTIFICATION_RECEIVED);
        intentFilter.addAction(Constants.PUSH_NOTIFICATION_DELETED);
        LocalBroadcastManager.getInstance(view.getApplicationContext()).registerReceiver(messageReceiver, intentFilter);
    }

    private void resetState(){
        page                   = 1;
        loadedAllNotifications = false;
        notifications.clear();
    }

    @Override
    public void loadDataByTerm(String term){
        this.term = term;
        resetState();
        loadData();
    }

    public PushNotificationsListPresenter(ISecurityManager securityManager, IPushNotificationsListInteractor interactor, IPushNotificationsWireframe wireframe) {
        super(interactor, wireframe);
        this.securityManager = securityManager;
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        loadedAllNotifications = false;
        loadData();
    }

    @Override
    public void loadData() {
        if (loadedAllNotifications) {
            return;
        }
        List<PushNotificationListItemDTO> notificationsPage = interactor.getNotifications(term, securityManager.getCurrentMember(), page, OBJECTS_PER_PAGE);
        notifications.addAll(notificationsPage);
        view.setNotifications(notifications);
        view.refresh();
        loadedAllNotifications = notificationsPage.size() < OBJECTS_PER_PAGE;
        page++;
    }

    @Override
    public int getObjectsPerPage() {
        return OBJECTS_PER_PAGE;
    }

    @Override
    public void showNotification(int position) {
        PushNotificationListItemDTO notification = notifications.get(position);
        if(!notification.isOpened()){
            interactor.markAsOpen(notification.getId());
            notification.setOpened(true);
            view.refresh();
            Intent openedIntent = new Intent(Constants.PUSH_NOTIFICATION_OPENED);
            LocalBroadcastManager.getInstance(view.getApplicationContext()).sendBroadcast(openedIntent);
        }
        wireframe.showNotification(notification.getId(), view);
    }

    @Override
    public void buildItem(IPushNotificationItemView itemView, int position) {
        PushNotificationListItemDTO notification = notifications.get(position);
        itemView.setSubject(notification.getSubject());
        itemView.setBody(notification.getBody());
        itemView.setOpened(notification.isOpened());
        itemView.setReceivedDate(notification.getReceivedDate());
    }

    @Override
    public void onRemovePushNotification(PushNotificationListItemDTO item) {
        interactor.deleteNotification(item);
        notifications.remove(item);
        NotificationManager notificationManager = (NotificationManager) view.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(item.getId());
        Intent deletedIntent = new Intent(Constants.PUSH_NOTIFICATION_DELETED);
        LocalBroadcastManager.getInstance(view.getApplicationContext()).sendBroadcast(deletedIntent);
    }
}
