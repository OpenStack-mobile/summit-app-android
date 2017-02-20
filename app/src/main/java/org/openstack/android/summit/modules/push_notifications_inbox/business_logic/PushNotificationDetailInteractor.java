package org.openstack.android.summit.modules.push_notifications_inbox.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.PushNotificationDetailDTO;
import org.openstack.android.summit.common.DTOs.PushNotificationListItemDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.repositories.IPushNotificationDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.entities.notifications.PushNotification;
import org.openstack.android.summit.common.security.ISecurityManager;

/**
 * Created by sebastian on 8/22/2016.
 */
public class PushNotificationDetailInteractor
        extends BaseInteractor
        implements IPushNotificationDetailInteractor {

    private IPushNotificationDataStore dataStore;

    public PushNotificationDetailInteractor
    (
        ISecurityManager securityManager,
        IPushNotificationDataStore dataStore,
        IDTOAssembler dtoAssembler,
        ISummitDataStore summitDataStore,
        ISummitSelector summitSelector
    ) {
        super(securityManager, dtoAssembler, summitSelector, summitDataStore);
        this.dataStore = dataStore;
    }

    @Override
    public PushNotificationDetailDTO getPushNotificationDetail(int pushNotificationId) {
        PushNotification entity = dataStore.getById(pushNotificationId);
        if(entity == null) return null;
        return dtoAssembler.createDTO(entity, PushNotificationDetailDTO.class);
    }

    @Override
    public void deleteNotification(PushNotificationListItemDTO notification) {
        dataStore.delete(notification.getId());
    }

}
