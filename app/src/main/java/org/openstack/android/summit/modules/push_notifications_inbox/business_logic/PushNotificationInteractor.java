package org.openstack.android.summit.modules.push_notifications_inbox.business_logic;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.repositories.IPushNotificationDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ITeamPushNotificationDataStore;
import org.openstack.android.summit.common.entities.notifications.IPushNotification;
import org.openstack.android.summit.common.entities.notifications.PushNotification;
import org.openstack.android.summit.common.entities.notifications.TeamPushNotification;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.utils.RealmFactory;

import io.realm.Realm;

/**
 * Created by smarcet on 1/24/17.
 */

public class PushNotificationInteractor extends BaseInteractor implements IPushNotificationInteractor {

    private IPushNotificationDataStore pushNotificationDataStore;
    private ITeamPushNotificationDataStore teamPushNotificationDataStore;

    public PushNotificationInteractor(
            ISecurityManager securityManager,
            IPushNotificationDataStore pushNotificationDataStore,
            ITeamPushNotificationDataStore teamPushNotificationDataStore,
            ISummitDataStore summitDataStore,
            ISummitSelector summitSelector,
            IReachability reachability
    ){
        super(securityManager, null, summitSelector, summitDataStore, reachability);
        this.pushNotificationDataStore      = pushNotificationDataStore;
        this.teamPushNotificationDataStore  = teamPushNotificationDataStore;
    }

    @Override
    public IPushNotification save(final IPushNotification pushNotification) {

        try {
            return RealmFactory.transaction(new RealmFactory.IRealmCallback<IPushNotification>() {
                @Override
                public IPushNotification callback(Realm session) throws Exception {

                    if (pushNotification instanceof TeamPushNotification) {
                        teamPushNotificationDataStore.saveOrUpdate((TeamPushNotification) pushNotification);
                    }
                    else{
                        pushNotificationDataStore.saveOrUpdate((PushNotification) pushNotification);
                    }
                    return pushNotification;
                }
            });
        }
        catch (Exception ex){
            Log.e(Constants.LOG_TAG, ex.getMessage());
            Crashlytics.logException(ex);
        }
        return null;
    }
}
