package org.openstack.android.summit.dagger.modules;

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
    ILevelListInteractor providesLevelListInteractor(ISummitEventDataStore summitEventDataStore) {
        return new LevelListInteractor(summitEventDataStore);
    }

    @Provides
    ILevelListPresenter providesLevelListPresenter(ILevelListInteractor levelListInteractor, ILevelListWireframe levelListWireframe) {
        return new LevelListPresenter(levelListInteractor, levelListWireframe);
    }
}
