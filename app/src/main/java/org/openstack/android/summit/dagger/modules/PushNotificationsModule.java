package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.IEventPushNotificationDataStore;
import org.openstack.android.summit.common.data_access.repositories.IPushNotificationDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.repositories.ITeamDataStore;
import org.openstack.android.summit.common.data_access.repositories.ITeamPushNotificationDataStore;
import org.openstack.android.summit.common.entities.notifications.IPushNotificationFactory;
import org.openstack.android.summit.common.entities.notifications.PushNotificationFactory;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.modules.push_notifications_inbox.business_logic.IPushNotificationInteractor;
import org.openstack.android.summit.modules.push_notifications_inbox.business_logic.PushNotificationInteractor;
import org.openstack.android.summit.modules.push_notifications_inbox.user_interface.PushPushNotificationsListFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by smarcet on 1/24/17.
 */
@Module
public class PushNotificationsModule {

    @Provides
    PushPushNotificationsListFragment providesPushPushNotificationsListFragment(){
        return new PushPushNotificationsListFragment();
    }

    @Provides
    IPushNotificationInteractor providesPushNotificationInteractor
    (
            ISecurityManager securityManager,
            IDTOAssembler dtoAssembler,
            IPushNotificationDataStore pushNotificationDataStore,
            ITeamPushNotificationDataStore teamPushNotificationDataStore,
            IEventPushNotificationDataStore eventPushNotificationDataStore,
            ISummitSelector summitSelector,
            ISummitDataStore summitDataStore
    )
    {
        return new PushNotificationInteractor
        (
            securityManager,
            dtoAssembler,
            pushNotificationDataStore,
            teamPushNotificationDataStore,
            eventPushNotificationDataStore,
            summitDataStore,
            summitSelector
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
