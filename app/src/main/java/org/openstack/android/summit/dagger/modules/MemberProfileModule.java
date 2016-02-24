package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.member_profile.IMemberProfileWireframe;
import org.openstack.android.summit.modules.member_profile.MemberProfileWireframe;
import org.openstack.android.summit.modules.member_profile.business_logic.IMemberProfileInteractor;
import org.openstack.android.summit.modules.member_profile.business_logic.MemberProfileInteractor;
import org.openstack.android.summit.modules.member_profile.user_interface.IMemberProfilePresenter;
import org.openstack.android.summit.modules.member_profile.user_interface.MemberProfileFragment;
import org.openstack.android.summit.modules.member_profile.user_interface.MemberProfilePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 1/26/2016.
 */
@Module
public class MemberProfileModule {
    @Provides
    MemberProfileFragment providesMemberProfileFragment() {
        return new MemberProfileFragment();
    }

    @Provides
    IMemberProfileWireframe providesMemberProfileWireframe(INavigationParametersStore navigationParametersStore) {
        return new MemberProfileWireframe(navigationParametersStore);
    }

    @Provides
    IMemberProfileInteractor providesMemberProfileInteractor(IGenericDataStore genericDataStore, ISecurityManager securityManager, IDTOAssembler dtoAssembler, IDataUpdatePoller dataUpdatePoller) {
        return new MemberProfileInteractor(genericDataStore, securityManager, dtoAssembler, dataUpdatePoller);
    }

    @Provides
    IMemberProfilePresenter providesMemberProfilePresenter(IMemberProfileInteractor memberProfileInteractor, IMemberProfileWireframe memberProfileWireframe) {
        return new MemberProfilePresenter(memberProfileInteractor, memberProfileWireframe);
    }    
}
