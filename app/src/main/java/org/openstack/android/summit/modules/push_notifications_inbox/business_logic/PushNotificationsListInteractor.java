package org.openstack.android.summit.modules.push_notifications_inbox.business_logic;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.PushNotificationListItemDTO;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.repositories.IPushNotificationDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.entities.notifications.PushNotification;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import java.security.InvalidParameterException;
import java.util.List;

import io.realm.Realm;
import io.reactivex.Observable;

/**
 * Created by sebastian on 8/20/2016.
 */
public class PushNotificationsListInteractor
        extends BaseInteractor implements IPushNotificationsListInteractor {

    private IPushNotificationDataStore pushNotificationDataStore;

    private ISession session;

    public PushNotificationsListInteractor
    (
        ISecurityManager securityManager,
        IPushNotificationDataStore pushNotificationDataStore,
        IDTOAssembler dtoAssembler,
        ISummitDataStore summitDataStore,
        ISummitSelector summitSelector,
        ISession session,
        IReachability reachability
    )
    {
        super(securityManager, dtoAssembler, summitSelector, summitDataStore, reachability);
        this.pushNotificationDataStore = pushNotificationDataStore;
        this.session                   = session;
    }

    @Override
    public Observable<List<PushNotificationListItemDTO>> getNotifications(String term, Integer memberId, int page, int objectsPerPage) {
        // try to get remote
        try {
            return pushNotificationDataStore
                    .getByFilterRemote(term, memberId, page, objectsPerPage)
                    .map(list -> createDTOList(list, PushNotificationListItemDTO.class));
        }
        catch (Exception ex){
            Log.e(Constants.LOG_TAG, ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<PushNotificationListItemDTO> getLocalNotifications(String term, Integer memberId, int page, int objectsPerPage) {
        return createDTOList(pushNotificationDataStore.getByFilter(term, memberId, page, objectsPerPage), PushNotificationListItemDTO.class);
    }

    @Override
    public void deleteNotification(PushNotificationListItemDTO notification) {
        pushNotificationDataStore.delete(notification.getId());
    }

    @Override
    public void markAsOpen(final int notificationId) {
        try {
            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception {
                    PushNotification entity = pushNotificationDataStore.getById(notificationId);
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

    @Override
    public void setBlockAllNotifications(boolean block) {
        session.setInt(Constants.SETTING_BLOCK_NOTIFICATIONS_KEY, block ? 1: 0);
    }

    @Override
    public boolean getBlockAllNotifications() {
        return session.getInt(Constants.SETTING_BLOCK_NOTIFICATIONS_KEY) == 1;
    }
}
