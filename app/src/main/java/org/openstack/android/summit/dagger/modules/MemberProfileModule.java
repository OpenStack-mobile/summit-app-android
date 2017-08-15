package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.IPresentationSpeakerDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.modules.events.IEventsWireframe;
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
    IMemberProfileWireframe providesMemberProfileWireframe(INavigationParametersStore navigationParametersStore, IEventsWireframe eventsWireframe) {
        return new MemberProfileWireframe(navigationParametersStore, eventsWireframe);
    }

    @Provides
    IMemberProfileInteractor providesMemberProfileInteractor(IPresentationSpeakerDataStore presentationSpeakerDataStore, ISecurityManager securityManager, IDTOAssembler dtoAssembler, ISummitDataStore summitDataStore, ISummitSelector summitSelector, IReachability reachability) {
        return new MemberProfileInteractor(presentationSpeakerDataStore, securityManager, dtoAssembler, summitDataStore, summitSelector, reachability);
    }

    @Provides
    IMemberProfilePresenter providesMemberProfilePresenter(IMemberProfileInteractor memberProfileInteractor, IMemberProfileWireframe memberProfileWireframe) {
        return new MemberProfilePresenter(memberProfileInteractor, memberProfileWireframe);
    }    
}
