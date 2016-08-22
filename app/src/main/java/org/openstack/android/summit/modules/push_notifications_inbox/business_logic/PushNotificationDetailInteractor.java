package org.openstack.android.summit.modules.push_notifications_inbox.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.PushNotificationDetailDTO;
import org.openstack.android.summit.common.DTOs.PushNotificationListItemDTO;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.IPushNotificationDataStore;
import org.openstack.android.summit.common.entities.PushNotification;

/**
 * Created by sebastian on 8/22/2016.
 */
public class PushNotificationDetailInteractor
        extends BaseInteractor
        implements IPushNotificationDetailInteractor {

    private IPushNotificationDataStore dataStore;

    public PushNotificationDetailInteractor(IPushNotificationDataStore dataStore, IDTOAssembler dtoAssembler) {
        super(dtoAssembler);
        this.dataStore = dataStore;
    }

    @Override
    public PushNotificationDetailDTO getPushNotificationDetail(int pushNotificationId) {
        PushNotification entity = dataStore.getByIdLocal(pushNotificationId, PushNotification.class);
        if(entity == null) return null;
        return dtoAssembler.createDTO(entity, PushNotificationDetailDTO.class);
    }

    @Override
    public void deleteNotification(PushNotificationListItemDTO notification) {
        dataStore.delete(notification.getId(), null, PushNotification.class);
    }

}
