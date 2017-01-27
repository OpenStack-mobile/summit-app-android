package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import org.openstack.android.summit.common.DTOs.PushNotificationDetailDTO;
import org.openstack.android.summit.common.entities.notifications.PushNotification;

/**
 * Created by sebastian on 8/22/2016.
 */
public class PushNotification2PushNotificationDetailDTO extends AbstractPushNotification2PushNotificationListItemDTO<PushNotification, PushNotificationDetailDTO> {

    @Override
    protected PushNotificationDetailDTO createDTO() {
        return new PushNotificationDetailDTO();
    }
}
