package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.security.ISecurityManager;
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
                                               ISummitSelector summitSelector){
        return new SplashInteractor(summitDataStore, securityManager, dtoAssembler, summitSelector);
    }

    @Provides
    ISplashPresenter providesSplashPresenter(ISplashInteractor interactor, ISplashWireframe wireframe){
        return new SplashPresenter(interactor, wireframe);
    }
}
