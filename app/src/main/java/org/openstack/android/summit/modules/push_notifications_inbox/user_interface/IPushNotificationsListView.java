package org.openstack.android.summit.modules.push_notifications_inbox.user_interface;

import org.openstack.android.summit.common.DTOs.PushNotificationListItemDTO;
import org.openstack.android.summit.common.user_interface.IBaseView;
import java.util.List;

/**
 * Created by sebastian on 8/19/2016.
 */
public interface IPushNotificationsListView extends IBaseView {

    void setNotifications(List<PushNotificationListItemDTO> notifications);

    void refresh();

    void setSwitchEnableNotificationsState(boolean checked);
}
