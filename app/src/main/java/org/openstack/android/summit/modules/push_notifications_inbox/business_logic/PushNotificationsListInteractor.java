package org.openstack.android.summit.modules.push_notifications_inbox.business_logic;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.PushNotificationListItemDTO;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.IPushNotificationDataStore;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.PushNotification;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import java.security.InvalidParameterException;
import java.util.List;

import io.realm.Realm;

/**
 * Created by sebastian on 8/20/2016.
 */
public class PushNotificationsListInteractor extends BaseInteractor implements IPushNotificationsListInteractor {

    private IPushNotificationDataStore pushNotificationDataStore;

    public PushNotificationsListInteractor(IPushNotificationDataStore pushNotificationDataStore, IDTOAssembler dtoAssembler) {
        super(dtoAssembler);
        this.pushNotificationDataStore = pushNotificationDataStore;
    }

    @Override
    public List<PushNotificationListItemDTO> getNotifications(String term, Member member, int page, int objectsPerPage) {
        List<PushNotification> notifications = pushNotificationDataStore.getByFilterLocal(term, member, page, objectsPerPage);
        return createDTOList(notifications, PushNotificationListItemDTO.class);
    }

    @Override
    public void deleteNotification(PushNotificationListItemDTO notification) {
        pushNotificationDataStore.delete(notification.getId(), null, PushNotification.class);
    }

    @Override
    public void markAsOpen(final int notificationId) {
        try {
            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception {
                    PushNotification entity = pushNotificationDataStore.getByIdLocal(notificationId, PushNotification.class);
                    if (entity == null)
                        throw new InvalidParameterException("missing push notification!");
                    entity.setOpened(true);
                    return Void.getInstance();
                }
            });
        }
        catch(Exception ex){
            Log.e(Constants.LOG_TAG, ex.getMessage());
            Crashlytics.logException(ex);
        }
    }
}
