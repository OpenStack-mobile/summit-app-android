package org.openstack.android.summit.dagger.components;

import android.app.Activity;

import org.openstack.android.summit.dagger.PerActivity;
import org.openstack.android.summit.dagger.modules.ActivityModule;
import org.openstack.android.summit.dagger.modules.EventsModule;

import dagger.Component;

/**
 * Created by Claudio Redi on 11/3/2015.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = { ActivityModule.class, EventsModule.class })
public interface ActivityComponent {
    //Exposed to sub-graphs.
    Activity activity();
}