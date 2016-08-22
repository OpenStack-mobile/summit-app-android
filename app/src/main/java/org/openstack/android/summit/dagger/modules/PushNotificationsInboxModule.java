package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.data_access.IPushNotificationDataStore;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.modules.main.IMainWireframe;
import org.openstack.android.summit.modules.push_notifications_inbox.IPushNotificationsWireframe;
import org.openstack.android.summit.modules.push_notifications_inbox.PushPushNotificationsWireframe;
import org.openstack.android.summit.modules.push_notifications_inbox.business_logic.IPushNotificationDetailInteractor;
import org.openstack.android.summit.modules.push_notifications_inbox.business_logic.IPushNotificationsListInteractor;
import org.openstack.android.summit.modules.push_notifications_inbox.business_logic.PushNotificationDetailInteractor;
import org.openstack.android.summit.modules.push_notifications_inbox.business_logic.PushNotificationsListInteractor;
import org.openstack.android.summit.modules.push_notifications_inbox.user_interface.IPushNotificationDetailPresenter;
import org.openstack.android.summit.modules.push_notifications_inbox.user_interface.IPushNotificationDetailView;
import org.openstack.android.summit.modules.push_notifications_inbox.user_interface.IPushNotificationDetailWireframe;
import org.openstack.android.summit.modules.push_notifications_inbox.user_interface.IPushNotificationsListPresenter;
import org.openstack.android.summit.modules.push_notifications_inbox.user_interface.IPushNotificationsListView;
import org.openstack.android.summit.modules.push_notifications_inbox.user_interface.PushNotificationDetailFragment;
import org.openstack.android.summit.modules.push_notifications_inbox.user_interface.PushNotificationDetailPresenter;
import org.openstack.android.summit.modules.push_notifications_inbox.user_interface.PushNotificationDetailWireframe;
import org.openstack.android.summit.modules.push_notifications_inbox.user_interface.PushNotificationsListPresenter;
import org.openstack.android.summit.modules.push_notifications_inbox.user_interface.PushPushNotificationsListFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sebastian on 8/19/2016.
 */
@Module
public class PushNotificationsInboxModule {

    @Provides
    IPushNotificationsListView providesNotificationsListFragment() {
        return new PushPushNotificationsListFragment();
    }

    @Provides
    IPushNotificationDetailView providesPushNotificationDetailFragment() {
        return new PushNotificationDetailFragment();
    }

    @Provides
    IPushNotificationsWireframe providesNotificationsWireframe(INavigationParametersStore navigationParametersStore) {
        return new PushPushNotificationsWireframe(navigationParametersStore);
    }

    @Provides
    IPushNotificationDetailWireframe providesPushNotificationDetailWireframe(IMainWireframe mainWireframe, INavigationParametersStore navigationParametersStore) {
        return new PushNotificationDetailWireframe(mainWireframe, navigationParametersStore);
    }

    @Provides
    IPushNotificationsListInteractor providesNotificationsListInteractor(IPushNotificationDataStore pushNotificationDataStore, IDTOAssembler dtoAssembler) {
        return new PushNotificationsListInteractor(pushNotificationDataStore, dtoAssembler);
    }

    @Provides
    IPushNotificationDetailInteractor providesPushNotificationDetailInteractor(IPushNotificationDataStore pushNotificationDataStore, IDTOAssembler dtoAssembler) {
        return new PushNotificationDetailInteractor(pushNotificationDataStore, dtoAssembler);
    }

    @Provides
    IPushNotificationsListPresenter providesNotificationsListPresenter(ISecurityManager securityManager, IPushNotificationsListInteractor interactor, IPushNotificationsWireframe wireframe) {
        return new PushNotificationsListPresenter(securityManager, interactor, wireframe);
    }

    @Provides
    IPushNotificationDetailPresenter providesPushNotificationDetailPresenter(IPushNotificationDetailInteractor interactor, IPushNotificationDetailWireframe wireframe) {
        return new PushNotificationDetailPresenter(interactor, wireframe);
    }
}
