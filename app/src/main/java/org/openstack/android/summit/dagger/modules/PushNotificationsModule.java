package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.IPushNotificationDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.repositories.ITeamDataStore;
import org.openstack.android.summit.common.data_access.repositories.ITeamPushNotificationDataStore;
import org.openstack.android.summit.common.entities.notifications.IPushNotificationFactory;
import org.openstack.android.summit.common.entities.notifications.PushNotificationFactory;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.modules.push_notifications_inbox.business_logic.IPushNotificationInteractor;
import org.openstack.android.summit.modules.push_notifications_inbox.business_logic.PushNotificationInteractor;

import dagger.Module;
import dagger.Provides;

/**
 * Created by smarcet on 1/24/17.
 */
@Module
public class PushNotificationsModule {

    @Provides
    IPushNotificationInteractor providesPushNotificationInteractor
    (
            ISecurityManager securityManager,
            IPushNotificationDataStore pushNotificationDataStore,
            ITeamPushNotificationDataStore teamPushNotificationDataStore,
            ISummitSelector summitSelector,
            ISummitDataStore summitDataStore,
            IReachability reachability
    )
    {
        return new PushNotificationInteractor
                (
                        securityManager,
                        pushNotificationDataStore,
                        teamPushNotificationDataStore,
                        summitDataStore,
                        summitSelector,
                        reachability
                );
    }

    @Provides
    IPushNotificationFactory providesPushNotificationFactory
    (
        ISummitDataStore summitDataStore,
        ISummitEventDataStore eventDataStore,
        ITeamDataStore teamDataStore
    )
    {
        return new PushNotificationFactory(summitDataStore, eventDataStore, teamDataStore);
    }
}
