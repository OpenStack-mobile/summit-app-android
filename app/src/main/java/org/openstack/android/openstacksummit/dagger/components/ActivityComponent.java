package org.openstack.android.openstacksummit.dagger.components;

import android.app.Activity;

import org.openstack.android.openstacksummit.dagger.PerActivity;
import org.openstack.android.openstacksummit.dagger.modules.ActivityModule;
import org.openstack.android.openstacksummit.dagger.modules.GeneralScheduleModule;

import dagger.Component;

/**
 * Created by Claudio Redi on 11/3/2015.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = { ActivityModule.class, GeneralScheduleModule.class })
public interface ActivityComponent {
    //Exposed to sub-graphs.
    Activity activity();
}