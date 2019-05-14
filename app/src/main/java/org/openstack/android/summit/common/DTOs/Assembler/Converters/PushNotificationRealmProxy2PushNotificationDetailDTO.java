package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import org.openstack.android.summit.common.DTOs.PushNotificationDetailDTO;

import io.realm.org_openstack_android_summit_common_entities_notifications_PushNotificationRealmProxy;

/**
 * Created by sebastian on 8/22/2016.
 */
public class PushNotificationRealmProxy2PushNotificationDetailDTO extends AbstractPushNotification2PushNotificationListItemDTO<org_openstack_android_summit_common_entities_notifications_PushNotificationRealmProxy, PushNotificationDetailDTO> {

    @Override
    protected PushNotificationDetailDTO createDTO() {
        return new PushNotificationDetailDTO();
    }
}

