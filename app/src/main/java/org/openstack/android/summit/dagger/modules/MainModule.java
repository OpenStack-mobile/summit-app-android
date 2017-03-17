package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.IPushNotificationDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.utils.IAppLinkRouter;
import org.openstack.android.summit.modules.about.IAboutWireframe;
import org.openstack.android.summit.modules.events.IEventsWireframe;
import org.openstack.android.summit.modules.main.IMainWireframe;
import org.openstack.android.summit.modules.main.MainWireframe;
import org.openstack.android.summit.modules.main.business_logic.DataLoadingInteractor;
import org.openstack.android.summit.modules.main.business_logic.IDataLoadingInteractor;
import org.openstack.android.summit.modules.main.business_logic.IMainInteractor;
import org.openstack.android.summit.modules.main.business_logic.MainInteractor;
import org.openstack.android.summit.modules.main.user_interface.IDataLoadingPresenter;
import org.openstack.android.summit.modules.main.user_interface.IMainPresenter;
import org.openstack.android.summit.modules.main.user_interface.MainPresenter;
import org.openstack.android.summit.modules.main.user_interface.SummitListDataLoadingPresenter;
import org.openstack.android.summit.modules.member_profile.IMemberProfileWireframe;
import org.openstack.android.summit.modules.push_notifications_inbox.IPushNotificationsWireframe;
import org.openstack.android.summit.modules.search.ISearchWireframe;
import org.openstack.android.summit.modules.settings.ISettingsWireframe;
import org.openstack.android.summit.modules.speakers_list.ISpeakerListWireframe;
import org.openstack.android.summit.modules.venues.IVenuesWireframe;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
@Module
public class MainModule {

    @Provides
    IMainWireframe providesMainWireframe(IEventsWireframe eventsWireframe, ISpeakerListWireframe speakerListWireframe, IMemberProfileWireframe memberProfileWireframe, ISearchWireframe searchWireframe, IVenuesWireframe venuesWireframe, IAboutWireframe aboutWireframe, IPushNotificationsWireframe nofificationsWireframe, ISettingsWireframe settingsWireframe) {
        return new MainWireframe(eventsWireframe, speakerListWireframe, memberProfileWireframe, searchWireframe, venuesWireframe, aboutWireframe, nofificationsWireframe, settingsWireframe);
    }

    @Provides
    IMainInteractor providesMainInteractor(ISummitDataStore summitDataStore, ISecurityManager securityManager, IPushNotificationsManager pushNotificationsManager, IDTOAssembler dtoAssembler, IReachability reachability, IPushNotificationDataStore pushNotificationDataStore, ISession session, ISummitSelector summitSelector) {
        return new MainInteractor(summitDataStore, securityManager, pushNotificationsManager, dtoAssembler, reachability, pushNotificationDataStore, session, summitSelector);
    }

    @Provides
    IMainPresenter providesMainPresenter
    (
        IMainInteractor interactor,
        IMainWireframe wireframe,
        IAppLinkRouter appLinkRouter,
        ISecurityManager securityManager,
        IReachability reachability,
        ISession session
    )
    {
        return new MainPresenter(interactor, wireframe, appLinkRouter, securityManager, reachability, session);
    }

    @Provides
    IDataLoadingInteractor providesDataLoadingInteractor(ISecurityManager securityManager, IDTOAssembler dtoAssembler, ISummitSelector summitSelector, ISummitDataStore summitDataStore) {
        return new DataLoadingInteractor(securityManager, dtoAssembler, summitSelector, summitDataStore);
    }

    @Provides
    @Named("SummitListDataLoading")
    IDataLoadingPresenter providesDataLoadingPresenter(IDataLoadingInteractor dataLoadingInteractor, ISummitSelector summitSelector){
        return new SummitListDataLoadingPresenter(dataLoadingInteractor, null, summitSelector);
    }

}
