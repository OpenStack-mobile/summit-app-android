package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.business_logic.IScheduleInteractor;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.events.EventsWireframe;
import org.openstack.android.summit.modules.events.IEventsWireframe;
import org.openstack.android.summit.modules.events.business_logic.EventsInteractor;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 11/3/2015.
 */
@Module
public class EventsModule {
    @Provides
    IEventsWireframe providesGeneralScheduleWireframe(IEventDetailWireframe eventDetailWireframe) {
        return new EventsWireframe(eventDetailWireframe);
    }

    @Provides
    IScheduleInteractor providesEventsInteractor(ISummitDataStore summitDataStore, IDTOAssembler dtoAssembler) {
        return new EventsInteractor(summitDataStore, dtoAssembler);
    }
}

