package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import org.openstack.android.summit.common.DTOs.PushNotificationListItemDTO;

/**
 * Created by sebastian on 8/20/2016.
 */
public class PushNotificationRealmProxy2PushNotificationListItemDTO extends AbstractPushNotification2PushNotificationListItemDTO<io.realm.org_openstack_android_summit_common_entities_notifications_PushNotificationRealmProxy, PushNotificationListItemDTO> {

    @Override
    protected PushNotificationListItemDTO createDTO() {
        return new PushNotificationListItemDTO();
    }
}
