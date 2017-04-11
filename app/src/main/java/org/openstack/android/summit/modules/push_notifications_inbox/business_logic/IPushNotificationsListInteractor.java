package org.openstack.android.summit.modules.push_notifications_inbox.business_logic;

import org.openstack.android.summit.common.DTOs.PushNotificationListItemDTO;
import org.openstack.android.summit.common.business_logic.IBaseInteractor;
import org.openstack.android.summit.common.entities.Member;
import java.util.List;

/**
 * Created by sebastian on 8/19/2016.
 */
public interface IPushNotificationsListInteractor extends IBaseInteractor, ISettingsInteractor {

     List<PushNotificationListItemDTO> getNotifications(String term, Member member, int page, int objectsPerPage);

     void deleteNotification(PushNotificationListItemDTO notification);

     void markAsOpen(int notificationId);
}
