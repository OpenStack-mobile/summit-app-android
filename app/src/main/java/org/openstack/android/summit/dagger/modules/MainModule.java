package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.utils.IAppLinkRouter;
import org.openstack.android.summit.modules.about.IAboutWireframe;
import org.openstack.android.summit.modules.events.IEventsWireframe;
import org.openstack.android.summit.modules.main.IMainWireframe;
import org.openstack.android.summit.modules.main.MainWireframe;
import org.openstack.android.summit.modules.main.business_logic.IMainInteractor;
import org.openstack.android.summit.modules.main.business_logic.MainInteractor;
import org.openstack.android.summit.modules.main.user_interface.IMainPresenter;
import org.openstack.android.summit.modules.main.user_interface.MainPresenter;
import org.openstack.android.summit.modules.member_order_confirm.IMemberOrderConfirmWireframe;
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
public class MainModule {

    @Provides
    IMainWireframe providesMainWireframe(IEventsWireframe eventsWireframe, ISpeakerListWireframe speakerListWireframe, IMemberProfileWireframe memberProfileWireframe, ISearchWireframe searchWireframe, IVenuesWireframe venuesWireframe, IMemberOrderConfirmWireframe memberOrderConfirmWireframe, IAboutWireframe aboutWireframe) {
        return new MainWireframe(eventsWireframe, speakerListWireframe, memberProfileWireframe, searchWireframe, venuesWireframe, memberOrderConfirmWireframe, aboutWireframe);
    }

    @Provides
    IMainInteractor providesMainInteractor(ISummitDataStore summitDataStore, ISecurityManager securityManager, IPushNotificationsManager pushNotificationsManager, IDTOAssembler dtoAssembler, IReachability reachability) {
        return new MainInteractor(summitDataStore, securityManager, pushNotificationsManager, dtoAssembler, reachability);
    }

    @Provides
    IMainPresenter providesMainPresenter(IMainInteractor interactor, IMainWireframe wireframe, IAppLinkRouter appLinkRouter) {
        return new MainPresenter(interactor, wireframe, appLinkRouter);
    }
}
