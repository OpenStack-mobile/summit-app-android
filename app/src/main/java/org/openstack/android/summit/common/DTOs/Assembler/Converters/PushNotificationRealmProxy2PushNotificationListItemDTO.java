package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import org.openstack.android.summit.common.DTOs.PushNotificationListItemDTO;

import io.realm.PushNotificationRealmProxy;

/**
 * Created by sebastian on 8/20/2016.
 */
public class PushNotificationRealmProxy2PushNotificationListItemDTO extends AbstractPushNotification2PushNotificationListItemDTO<PushNotificationRealmProxy, PushNotificationListItemDTO> {

    @Override
    protected PushNotificationListItemDTO createDTO() {
        return new PushNotificationListItemDTO();
    }
}
