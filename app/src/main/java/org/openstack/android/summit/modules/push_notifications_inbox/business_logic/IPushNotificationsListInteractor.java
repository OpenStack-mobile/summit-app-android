package org.openstack.android.summit.modules.push_notifications_inbox.business_logic;

import org.openstack.android.summit.common.DTOs.PushNotificationListItemDTO;
import org.openstack.android.summit.common.business_logic.IBaseInteractor;
import java.util.List;
import io.reactivex.Observable;

/**
 * Created by sebastian on 8/19/2016.
 */
public interface IPushNotificationsListInteractor extends IBaseInteractor, ISettingsInteractor {

     Observable<List<PushNotificationListItemDTO>> getNotifications(String term, Integer memberId, int page, int objectsPerPage);

     List<PushNotificationListItemDTO> getLocalNotifications(String term, Integer memberId, int page, int objectsPerPage);

     void deleteNotification(PushNotificationListItemDTO notification);

     void markAsOpen(int notificationId);
}
