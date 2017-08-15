package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.IMemberDataStore;
import org.openstack.android.summit.common.data_access.repositories.ITrackDataStore;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.ScheduleItemViewBuilder;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.general_schedule_filter.IGeneralScheduleFilterWireframe;
import org.openstack.android.summit.modules.rsvp.IRSVPWireframe;
import org.openstack.android.summit.modules.track_schedule.ITrackScheduleWireframe;
import org.openstack.android.summit.modules.track_schedule.TrackScheduleWireframe;
import org.openstack.android.summit.modules.track_schedule.business_logic.ITrackScheduleInteractor;
import org.openstack.android.summit.modules.track_schedule.business_logic.TrackScheduleInteractor;
import org.openstack.android.summit.modules.track_schedule.user_interface.ITrackSchedulePresenter;
import org.openstack.android.summit.modules.track_schedule.user_interface.TrackScheduleFragment;
import org.openstack.android.summit.modules.track_schedule.user_interface.TrackSchedulePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
@Module
public class TrackScheduleModule {
    @Provides
    TrackScheduleFragment providesTrackScheduleFragment() {
        return new TrackScheduleFragment();
    }

    @Provides
    ITrackScheduleWireframe providesTrackScheduleWireframe(IEventDetailWireframe eventDetailWireframe, IGeneralScheduleFilterWireframe generalScheduleFilterWireframe, IRSVPWireframe rsvpWireframe, INavigationParametersStore navigationParametersStore) {
        return new TrackScheduleWireframe(eventDetailWireframe, generalScheduleFilterWireframe, rsvpWireframe, navigationParametersStore);
    }

    @Provides
    ITrackScheduleInteractor providesTrackScheduleInteractor
    (
            IMemberDataStore memberDataStore,
            ISummitEventDataStore summitEventDataStore,
            ISummitDataStore summitDataStore,
            ITrackDataStore trackDataStore,
            IDTOAssembler dtoAssembler,
            ISecurityManager securityManager,
            IPushNotificationsManager pushNotificationsManager,
            ISession session,
            ISummitSelector summitSelector,
            IReachability reachability
    )
    {
        return new TrackScheduleInteractor(memberDataStore, summitEventDataStore, summitDataStore, trackDataStore, dtoAssembler, securityManager, pushNotificationsManager, session, summitSelector, reachability);
    }

    @Provides
    ITrackSchedulePresenter providesTrackSchedulePresenter(ITrackScheduleInteractor trackScheduleInteractor, ITrackScheduleWireframe trackScheduleWireframe, IScheduleablePresenter scheduleablePresenter, IScheduleFilter scheduleFilter) {
        return new TrackSchedulePresenter(trackScheduleInteractor, trackScheduleWireframe, scheduleablePresenter, new ScheduleItemViewBuilder(), scheduleFilter);
    }
}
