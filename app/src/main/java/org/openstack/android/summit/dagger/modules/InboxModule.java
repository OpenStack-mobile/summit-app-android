package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.modules.inbox.IInboxWireframe;
import org.openstack.android.summit.modules.inbox.InboxWireframe;
import org.openstack.android.summit.modules.inbox.business_logic.IInboxInteractor;
import org.openstack.android.summit.modules.inbox.business_logic.InboxInteractor;
import org.openstack.android.summit.modules.inbox.user_interface.IInboxPresenter;
import org.openstack.android.summit.modules.inbox.user_interface.InboxPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by smarcet on 2/7/17.
 */
@Module
public class InboxModule {

    @Provides
    IInboxInteractor providesInboxInteractor(ISecurityManager securityManager, IDTOAssembler dtoAssembler, ISummitSelector summitSelector, ISummitDataStore summitDataStore){
        return new InboxInteractor(securityManager, dtoAssembler, summitSelector, summitDataStore);
    }

    @Provides
    IInboxWireframe providesInboxWireframe(){
        return new InboxWireframe();
    }


    @Provides
    IInboxPresenter providesInboxPresenter(IInboxInteractor interactor, IInboxWireframe wireframe){
        return new InboxPresenter(interactor, wireframe);
    }
}
