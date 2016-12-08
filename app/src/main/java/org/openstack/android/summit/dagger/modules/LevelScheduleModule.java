package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.ScheduleItemViewBuilder;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.general_schedule_filter.IGeneralScheduleFilterWireframe;
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
    ILevelScheduleWireframe providesLevelScheduleWireframe(IEventDetailWireframe eventDetailWireframe, IGeneralScheduleFilterWireframe generalScheduleFilterWireframe, INavigationParametersStore navigationParametersStore) {
        return new LevelScheduleWireframe(eventDetailWireframe, generalScheduleFilterWireframe, navigationParametersStore);
    }

    @Provides
    ILevelScheduleInteractor providesLevelScheduleInteractor(ISummitEventDataStore summitEventDataStore, ISummitDataStore summitDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, IDTOAssembler dtoAssembler, ISecurityManager securityManager, IPushNotificationsManager pushNotificationsManager, ISession session, ISummitSelector summitSelector) {
        return new LevelScheduleInteractor(summitEventDataStore, summitDataStore, summitAttendeeDataStore, dtoAssembler, securityManager, pushNotificationsManager, session, summitSelector);
    }

    @Provides
    ILevelSchedulePresenter providesLevelSchedulePresenter(ILevelScheduleInteractor levelScheduleInteractor, ILevelScheduleWireframe levelScheduleWireframe, IScheduleablePresenter scheduleablePresenter, IScheduleFilter scheduleFilter) {
        return new LevelSchedulePresenter(levelScheduleInteractor, levelScheduleWireframe, scheduleablePresenter, new ScheduleItemViewBuilder(), scheduleFilter);
    }
}
