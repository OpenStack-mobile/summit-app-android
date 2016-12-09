package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.modules.level_list.ILevelListWireframe;
import org.openstack.android.summit.modules.level_list.LevelListWireframe;
import org.openstack.android.summit.modules.level_list.business_logic.ILevelListInteractor;
import org.openstack.android.summit.modules.level_list.business_logic.LevelListInteractor;
import org.openstack.android.summit.modules.level_list.user_interface.ILevelListPresenter;
import org.openstack.android.summit.modules.level_list.user_interface.LevelListFragment;
import org.openstack.android.summit.modules.level_list.user_interface.LevelListPresenter;
import org.openstack.android.summit.modules.level_schedule.ILevelScheduleWireframe;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 12/22/2015.
 */
@Module
public class LevelListModule {
    @Provides
    LevelListFragment providesLevelListFragment() {
        return new LevelListFragment();
    }

    @Provides
    ILevelListWireframe providesLevelListWireframe(ILevelScheduleWireframe levelScheduleWireframe) {
        return new LevelListWireframe(levelScheduleWireframe);
    }

    @Provides
    ILevelListInteractor providesLevelListInteractor(ISummitEventDataStore summitEventDataStore, IDTOAssembler dtoAssembler, ISummitDataStore summitDataStore, ISummitSelector summitSelector) {
        return new LevelListInteractor(summitEventDataStore, dtoAssembler, summitDataStore, summitSelector);
    }

    @Provides
    ILevelListPresenter providesLevelListPresenter(ILevelListInteractor levelListInteractor, ILevelListWireframe levelListWireframe, IScheduleFilter scheduleFilter) {
        return new LevelListPresenter(levelListInteractor, levelListWireframe, scheduleFilter);
    }
}
