package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.modules.events.EventsWireframe;
import org.openstack.android.summit.modules.events.IEventsWireframe;
import org.openstack.android.summit.modules.events.business_logic.EventsInteractor;
import org.openstack.android.summit.modules.events.business_logic.IEventsInteractor;
import org.openstack.android.summit.modules.events.user_interface.EventsFragment;
import org.openstack.android.summit.modules.events.user_interface.EventsPresenter;
import org.openstack.android.summit.modules.events.user_interface.IEventsPresenter;
import org.openstack.android.summit.modules.general_schedule_filter.IGeneralScheduleFilterWireframe;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 11/3/2015.
 */
@Module
public class EventsModule {
    @Provides
    EventsFragment providesEventsFragment() {
        return new EventsFragment();
    }

    @Provides
    IEventsWireframe providesEventsWireframe(IGeneralScheduleFilterWireframe generalScheduleFilterWireframe) {
        return new EventsWireframe(generalScheduleFilterWireframe);
    }

    @Provides
    IEventsInteractor providesEventsInteractor
    (
        ISummitDataStore summitDataStore,
        IReachability reachability,
        IDTOAssembler dtoAssembler,
        ISummitSelector  summitSelector
    ) {
        return new EventsInteractor(summitDataStore, reachability, dtoAssembler, summitSelector);
    }

    @Provides
    IEventsPresenter providesEventsPresenter(IEventsInteractor eventsInteractor, IEventsWireframe eventsWireframe, IScheduleFilter scheduleFilter) {
        return new EventsPresenter(eventsInteractor, eventsWireframe, scheduleFilter);
    }
}

