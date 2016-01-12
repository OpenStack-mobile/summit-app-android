package org.openstack.android.summit.dagger.components;

/**
 * Created by claudio on 11/3/2015.
 */

import org.openstack.android.summit.MainActivity;
import org.openstack.android.summit.dagger.modules.ApplicationModule;
import org.openstack.android.summit.dagger.modules.DTOAssemblerModule;
import org.openstack.android.summit.dagger.modules.DataAccessModule;
import org.openstack.android.summit.dagger.modules.EventDetailModule;
import org.openstack.android.summit.dagger.modules.EventsModule;
import org.openstack.android.summit.dagger.modules.GeneralScheduleModule;
import org.openstack.android.summit.dagger.modules.LevelListModule;
import org.openstack.android.summit.dagger.modules.LevelScheduleModule;
import org.openstack.android.summit.dagger.modules.SecurityModule;
import org.openstack.android.summit.dagger.modules.TrackListModule;
import org.openstack.android.summit.modules.events.user_interface.EventsFragment;
import org.openstack.android.summit.modules.general_schedule.user_interface.GeneralScheduleFragment;
import org.openstack.android.summit.modules.general_schedule.user_interface.IGeneralScheduleFragment;
import org.openstack.android.summit.modules.level_list.user_interface.ILevelListFragment;
import org.openstack.android.summit.modules.level_list.user_interface.LevelListFragment;
import org.openstack.android.summit.modules.level_schedule.user_interface.LevelScheduleFragment;

import javax.inject.Singleton;
import dagger.Component;

@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = { ApplicationModule.class, DataAccessModule.class, EventsModule.class, EventDetailModule.class, DTOAssemblerModule.class, SecurityModule.class, GeneralScheduleModule.class, TrackListModule.class, LevelListModule.class, LevelScheduleModule.class } )
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);
    void inject(EventsFragment eventsFragment);
    void inject(GeneralScheduleFragment generalScheduleFragment);
    void inject(LevelListFragment levelListFragment);
    void inject(LevelScheduleFragment levelScheduleFragment);
}
