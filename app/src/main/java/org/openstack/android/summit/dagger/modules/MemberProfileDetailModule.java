package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.IPresentationSpeakerDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.modules.member_order_confirm.IMemberOrderConfirmWireframe;
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
    IMemberProfileDetailWireframe providesMemberProfileDetailWireframe(IMemberOrderConfirmWireframe memberOrderConfirmWireframe, INavigationParametersStore navigationParametersStore) {
        return new MemberProfileDetailWireframe(memberOrderConfirmWireframe, navigationParametersStore);
    }

    @Provides
    IMemberProfileDetailInteractor providesMemberProfileDetailInteractor(IPresentationSpeakerDataStore presentationSpeakerDataStore, ISecurityManager securityManager, IDTOAssembler dtoAssembler, ISummitDataStore summitDataStore, ISummitSelector summitSelector) {
        return new MemberProfileDetailInteractor(presentationSpeakerDataStore, securityManager, dtoAssembler, summitDataStore, summitSelector);
    }

    @Provides
    IMemberProfileDetailPresenter providesMemberProfileDetailPresenter(IMemberProfileDetailInteractor interactor, IMemberProfileDetailWireframe wireframe, ISession session) {
        return new MemberProfileDetailPresenter(interactor, wireframe, session);
    }
}
