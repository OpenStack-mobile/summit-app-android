package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.modules.level_list.user_interface.LevelListFragment;

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
}
