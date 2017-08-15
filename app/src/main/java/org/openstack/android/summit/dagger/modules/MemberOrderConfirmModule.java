package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.IMemberRemoteDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.modules.member_order_confirm.IMemberOrderConfirmWireframe;
import org.openstack.android.summit.modules.member_order_confirm.MemberOrderConfirmWireframe;
import org.openstack.android.summit.modules.member_order_confirm.business_logic.IMemberOrderConfirmInteractor;
import org.openstack.android.summit.modules.member_order_confirm.business_logic.MemberOrderConfirmInteractor;
import org.openstack.android.summit.modules.member_order_confirm.user_interface.IMemberOrderConfirmPresenter;
import org.openstack.android.summit.modules.member_order_confirm.user_interface.MemberOrderConfirmFragment;
import org.openstack.android.summit.modules.member_order_confirm.user_interface.MemberOrderConfirmPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 3/27/2016.
 */
@Module
public class MemberOrderConfirmModule {
    @Provides
    MemberOrderConfirmFragment providesMemberOrderConfirmFragment() {
        return new MemberOrderConfirmFragment();
    }

    @Provides
    IMemberOrderConfirmWireframe providesMemberOrderConfirmWireframe() {
        return new MemberOrderConfirmWireframe();
    }

    @Provides
    IMemberOrderConfirmInteractor providesMemberProfileDetailInteractor(IMemberRemoteDataStore memberRemoteDataStore, IReachability reachability, ISecurityManager securityManager, IDTOAssembler dtoAssembler, ISummitDataStore summitDataStore, ISummitSelector summitSelector) {
        return new MemberOrderConfirmInteractor(memberRemoteDataStore, securityManager, dtoAssembler, summitDataStore, summitSelector, reachability);
    }

    @Provides
    IMemberOrderConfirmPresenter providesMemberProfileDetailPresenter(IMemberOrderConfirmInteractor interactor, IMemberOrderConfirmWireframe wireframe) {
        return new MemberOrderConfirmPresenter(interactor, wireframe);
    }
}

