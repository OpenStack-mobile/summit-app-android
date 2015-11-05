package org.openstack.android.openstacksummit.dagger.components;

import org.openstack.android.openstacksummit.dagger.modules.DataAccessModule;
import org.openstack.android.openstacksummit.dagger.modules.GeneralScheduleModule;
import org.openstack.android.openstacksummit.dagger.PerFragment;
import org.openstack.android.openstacksummit.modules.general_schedule.user_interface.*;
import dagger.Component;

/**
 * Created by claudio on 11/3/2015.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = { DataAccessModule.class, GeneralScheduleModule.class })
public interface GeneralScheduleComponent {
    void inject(GeneralScheduleFragment generalScheduleFragment);
}
