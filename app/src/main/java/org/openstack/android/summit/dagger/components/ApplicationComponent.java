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
import org.openstack.android.summit.dagger.modules.ScheduleableModule;
import org.openstack.android.summit.dagger.modules.SearchModule;
import org.openstack.android.summit.dagger.modules.SecurityModule;
import org.openstack.android.summit.dagger.modules.SpeakerListModule;
import org.openstack.android.summit.dagger.modules.TrackListModule;
import org.openstack.android.summit.dagger.modules.TrackScheduleModule;
import org.openstack.android.summit.modules.events.user_interface.EventsFragment;
import org.openstack.android.summit.modules.general_schedule.user_interface.GeneralScheduleFragment;
import org.openstack.android.summit.modules.level_list.user_interface.LevelListFragment;
import org.openstack.android.summit.modules.level_schedule.user_interface.LevelScheduleFragment;
import org.openstack.android.summit.modules.search.user_interface.SearchFragment;
import org.openstack.android.summit.modules.speakers_list.user_interface.SpeakerListFragment;
import org.openstack.android.summit.modules.track_list.user_interface.TrackListFragment;
import org.openstack.android.summit.modules.track_schedule.user_interface.TrackScheduleFragment;

import javax.inject.Singleton;
import dagger.Component;

@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = { ApplicationModule.class, DataAccessModule.class, EventsModule.class, EventDetailModule.class, DTOAssemblerModule.class, SecurityModule.class, GeneralScheduleModule.class, TrackListModule.class, LevelListModule.class, LevelScheduleModule.class, TrackScheduleModule.class, SpeakerListModule.class, SearchModule.class, ScheduleableModule.class} )
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);
    void inject(EventsFragment eventsFragment);
    void inject(GeneralScheduleFragment generalScheduleFragment);
    void inject(LevelListFragment levelListFragment);
    void inject(LevelScheduleFragment levelScheduleFragment);
    void inject(TrackListFragment trackListFragment);
    void inject(TrackScheduleFragment trackScheduleFragment);
    void inject(SpeakerListFragment speakerListFragment);
    void inject(SearchFragment searchFragment);
}
