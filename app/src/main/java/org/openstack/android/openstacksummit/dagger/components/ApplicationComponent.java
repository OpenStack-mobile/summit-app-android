package org.openstack.android.openstacksummit.dagger.components;

/**
 * Created by claudio on 11/3/2015.
 */

import android.content.Context;
import org.openstack.android.openstacksummit.MainActivity;
import org.openstack.android.openstacksummit.dagger.modules.ApplicationModule;
import org.openstack.android.openstacksummit.dagger.modules.GeneralScheduleModule;
import org.openstack.android.openstacksummit.modules.general_schedule.user_interface.GeneralScheduleFragment;

import javax.inject.Singleton;
import dagger.Component;

@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = { ApplicationModule.class, GeneralScheduleModule.class } )
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);
    void inject(GeneralScheduleFragment generalScheduleFragment);
}
