package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
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
    ITrackScheduleWireframe providesTrackScheduleWireframe(IEventDetailWireframe eventDetailWireframe) {
        return new TrackScheduleWireframe(eventDetailWireframe);
    }

    @Provides
    ITrackScheduleInteractor providesTrackScheduleInteractor(ISummitEventDataStore summitEventDataStore, ISummitDataStore summitDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, IDTOAssembler dtoAssembler, ISecurityManager securityManager) {
        return new TrackScheduleInteractor(summitEventDataStore, summitDataStore, summitAttendeeDataStore, dtoAssembler, securityManager);
    }

    @Provides
    ITrackSchedulePresenter providesTrackSchedulePresenter(ITrackScheduleInteractor trackScheduleInteractor, ITrackScheduleWireframe trackScheduleWireframe) {
        return new TrackSchedulePresenter(trackScheduleInteractor, trackScheduleWireframe);
    }
}
