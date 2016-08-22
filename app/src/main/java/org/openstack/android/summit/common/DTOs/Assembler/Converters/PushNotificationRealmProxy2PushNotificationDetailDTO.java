package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import org.openstack.android.summit.common.DTOs.PushNotificationDetailDTO;

import io.realm.PushNotificationRealmProxy;

/**
 * Created by sebastian on 8/22/2016.
 */
public class PushNotificationRealmProxy2PushNotificationDetailDTO extends AbstractPushNotification2PushNotificationListItemDTO<PushNotificationRealmProxy, PushNotificationDetailDTO> {

    @Override
    protected PushNotificationDetailDTO createDTO() {
        return new PushNotificationDetailDTO();
    }
}

