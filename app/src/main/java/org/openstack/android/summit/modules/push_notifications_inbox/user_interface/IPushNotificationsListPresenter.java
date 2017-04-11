package org.openstack.android.summit.modules.push_notifications_inbox.user_interface;

import org.openstack.android.summit.common.DTOs.PushNotificationListItemDTO;
import org.openstack.android.summit.common.user_interface.IBasePresenter;

/**
 * Created by sebastian on 8/19/2016.
 */
public interface IPushNotificationsListPresenter extends IBasePresenter<IPushNotificationsListView> {

    void setView(IPushNotificationsListView notificationsListFragment);

    void loadData();

    int getObjectsPerPage();

    void showNotification(int position);

    void buildItem(IPushNotificationItemView itemView, int position);

    void onRemovePushNotification(PushNotificationListItemDTO item);

    void loadDataByTerm(String term);

    void setBlockAllNotifications(boolean block);
}
