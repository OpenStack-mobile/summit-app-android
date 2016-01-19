package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.ScheduleItemViewBuilder;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.level_list.ILevelListWireframe;
import org.openstack.android.summit.modules.level_list.user_interface.ILevelListPresenter;
import org.openstack.android.summit.modules.level_schedule.ILevelScheduleWireframe;
import org.openstack.android.summit.modules.level_schedule.LevelScheduleWireframe;
import org.openstack.android.summit.modules.level_schedule.business_logic.ILevelScheduleInteractor;
import org.openstack.android.summit.modules.level_schedule.business_logic.LevelScheduleInteractor;
import org.openstack.android.summit.modules.level_schedule.user_interface.ILevelSchedulePresenter;
import org.openstack.android.summit.modules.level_schedule.user_interface.LevelScheduleFragment;
import org.openstack.android.summit.modules.level_schedule.user_interface.LevelSchedulePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
@Module
public class LevelScheduleModule {
    @Provides
    LevelScheduleFragment providesLevelScheduleFragment() {
        return new LevelScheduleFragment();
    }

    @Provides
    ILevelScheduleWireframe providesLevelScheduleWireframe(IEventDetailWireframe eventDetailWireframe) {
        return new LevelScheduleWireframe(eventDetailWireframe);
    }

    @Provides
    ILevelScheduleInteractor providesLevelScheduleInteractor(ISummitEventDataStore summitEventDataStore, ISummitDataStore summitDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, IDTOAssembler dtoAssembler, ISecurityManager securityManager) {
        return new LevelScheduleInteractor(summitEventDataStore, summitDataStore, summitAttendeeDataStore, dtoAssembler, securityManager);
    }

    @Provides
    ILevelSchedulePresenter providesLevelSchedulePresenter(ILevelScheduleInteractor levelScheduleInteractor, ILevelScheduleWireframe levelScheduleWireframe, IScheduleablePresenter scheduleablePresenter) {
        return new LevelSchedulePresenter(levelScheduleInteractor, levelScheduleWireframe, scheduleablePresenter, new ScheduleItemViewBuilder());
    }
}
