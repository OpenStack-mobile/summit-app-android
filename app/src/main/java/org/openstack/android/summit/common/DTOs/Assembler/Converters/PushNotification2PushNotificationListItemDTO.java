package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import org.openstack.android.summit.common.DTOs.PushNotificationListItemDTO;
import org.openstack.android.summit.common.entities.notifications.PushNotification;

/**
 * Created by sebastian on 8/20/2016.
 */
public class PushNotification2PushNotificationListItemDTO extends AbstractPushNotification2PushNotificationListItemDTO<PushNotification, PushNotificationListItemDTO> {
    @Override
    protected PushNotificationListItemDTO createDTO() {
        return new PushNotificationListItemDTO();
    }
}
