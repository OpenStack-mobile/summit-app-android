package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.modules.events.IEventsWireframe;
import org.openstack.android.summit.modules.main_activity.IMainActivityWireframe;
import org.openstack.android.summit.modules.main_activity.MainActivityWireframe;
import org.openstack.android.summit.modules.main_activity.business_logic.IMainActivityInteractor;
import org.openstack.android.summit.modules.main_activity.business_logic.MainActivityInteractor;
import org.openstack.android.summit.modules.main_activity.user_interface.IMainActivityPresenter;
import org.openstack.android.summit.modules.main_activity.user_interface.MainActivityPresenter;
import org.openstack.android.summit.modules.member_profile.IMemberProfileWireframe;
import org.openstack.android.summit.modules.search.ISearchWireframe;
import org.openstack.android.summit.modules.speakers_list.ISpeakerListWireframe;
import org.openstack.android.summit.modules.venues.IVenuesWireframe;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
@Module
public class MainActivityModule {
    @Provides
    IMainActivityWireframe providesMainActivityWireframe(IEventsWireframe eventsWireframe, ISpeakerListWireframe speakerListWireframe, IMemberProfileWireframe memberProfileWireframe, ISearchWireframe searchWireframe, IVenuesWireframe venuesWireframe) {
        return new MainActivityWireframe(eventsWireframe, speakerListWireframe, memberProfileWireframe, searchWireframe, venuesWireframe);
    }

    @Provides
    IMainActivityInteractor providesMainActivityInteractor(ISecurityManager securityManager, IDTOAssembler dtoAssembler) {
        return new MainActivityInteractor(securityManager, dtoAssembler);
    }

    @Provides
    IMainActivityPresenter providesMainActivityPresenter(IMainActivityInteractor interactor, IMainActivityWireframe wireframe) {
        return new MainActivityPresenter(interactor, wireframe);
    }
}
