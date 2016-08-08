package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.modules.member_profile_detail.IMemberProfileDetailWireframe;
import org.openstack.android.summit.modules.member_profile_detail.MemberProfileDetailWireframe;
import org.openstack.android.summit.modules.member_profile_detail.business_logic.IMemberProfileDetailInteractor;
import org.openstack.android.summit.modules.member_profile_detail.business_logic.MemberProfileDetailInteractor;
import org.openstack.android.summit.modules.member_profile_detail.user_interface.IMemberProfileDetailPresenter;
import org.openstack.android.summit.modules.member_profile_detail.user_interface.MemberProfileDetailFragment;
import org.openstack.android.summit.modules.member_profile_detail.user_interface.MemberProfileDetailPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
@Module
public class MemberProfileDetailModule {
    @Provides
    MemberProfileDetailFragment providesMemberProfileDetailFragment() {
        return new MemberProfileDetailFragment();
    }

    @Provides
    IMemberProfileDetailWireframe providesMemberProfileDetailWireframe(INavigationParametersStore navigationParametersStore) {
        return new MemberProfileDetailWireframe(navigationParametersStore);
    }

    @Provides
    IMemberProfileDetailInteractor providesMemberProfileDetailInteractor(IGenericDataStore genericDataStore, ISecurityManager securityManager, IDTOAssembler dtoAssembler) {
        return new MemberProfileDetailInteractor(genericDataStore, securityManager, dtoAssembler);
    }

    @Provides
    IMemberProfileDetailPresenter providesMemberProfileDetailPresenter(IMemberProfileDetailInteractor interactor, IMemberProfileDetailWireframe wireframe) {
        return new MemberProfileDetailPresenter(interactor, wireframe);
    }
}
