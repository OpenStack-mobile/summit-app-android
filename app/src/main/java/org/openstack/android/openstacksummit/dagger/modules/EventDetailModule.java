package org.openstack.android.openstacksummit.dagger.modules;

import org.openstack.android.openstacksummit.modules.event_detail.EventDetailWireframe;
import org.openstack.android.openstacksummit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.openstacksummit.modules.general_schedule.GeneralScheduleWireframe;
import org.openstack.android.openstacksummit.modules.general_schedule.IGeneralScheduleWireframe;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
@Module
public class EventDetailModule {
    @Provides
    IEventDetailWireframe providesEventDetailWireframe() {
        return new EventDetailWireframe();
    }
}
