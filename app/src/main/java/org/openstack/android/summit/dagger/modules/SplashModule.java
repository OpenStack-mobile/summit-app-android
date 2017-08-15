package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.entities.notifications.IPushNotificationFactory;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.utils.IAppLinkRouter;
import org.openstack.android.summit.modules.push_notifications_inbox.business_logic.IPushNotificationInteractor;
import org.openstack.android.summit.modules.push_notifications_inbox.business_logic.IPushNotificationsListInteractor;
import org.openstack.android.summit.modules.splash.user_interface.ISplashPresenter;
import org.openstack.android.summit.modules.splash.ISplashWireframe;
import org.openstack.android.summit.modules.splash.user_interface.SplashPresenter;
import org.openstack.android.summit.modules.splash.SplashWireframe;
import org.openstack.android.summit.modules.splash.business_logic.ISplashInteractor;
import org.openstack.android.summit.modules.splash.business_logic.SplashInteractor;

import dagger.Module;
import dagger.Provides;

/**
 * Created by smarcet on 2/6/17.
 */
@Module
public class SplashModule {

    @Provides
    ISplashWireframe providesSplashWireframe(){
        return new SplashWireframe();
    }

    @Provides
    ISplashInteractor providesSplashInteractor(ISummitDataStore summitDataStore,
                                               ISecurityManager securityManager,
                                               IDTOAssembler dtoAssembler,
                                               ISession session,
                                               ISummitSelector summitSelector,
                                               IReachability reachability){
        return new SplashInteractor(summitDataStore, securityManager, dtoAssembler,session, summitSelector, reachability);
    }

    @Provides
    ISplashPresenter providesSplashPresenter
    (
        ISplashInteractor interactor,
        IPushNotificationInteractor pushNotificationInteractor,
        IPushNotificationsListInteractor pushNotificationsListInteractor,
        IPushNotificationFactory pushNotificationFactory,
        ISecurityManager securityManager,
        IAppLinkRouter appLinkRouter,
        ISplashWireframe wireframe,
        ISummitSelector summitSelector
    )
    {
        return new SplashPresenter(interactor, wireframe, pushNotificationInteractor, pushNotificationsListInteractor, pushNotificationFactory, securityManager, appLinkRouter, summitSelector);
    }
}
