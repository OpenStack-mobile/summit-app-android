package org.openstack.android.openstacksummit.dagger.modules;

import org.openstack.android.openstacksummit.dagger.PerActivity;
import org.openstack.android.openstacksummit.modules.event_detail.EventDetailWireframe;
import org.openstack.android.openstacksummit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.openstacksummit.modules.general_schedule.GeneralScheduleWireframe;
import org.openstack.android.openstacksummit.modules.general_schedule.IGeneralScheduleWireframe;
import org.openstack.android.openstacksummit.modules.general_schedule.business_logic.GeneralScheduleInteractor;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 11/3/2015.
 */
@Module
public class GeneralScheduleModule {
    @Provides
    IGeneralScheduleWireframe providesGeneralScheduleWireframe(IEventDetailWireframe eventDetailWireframe) {
        return new GeneralScheduleWireframe(eventDetailWireframe);
    }
}

