package org.openstack.android.openstacksummit;

import android.app.Application;

import org.openstack.android.openstacksummit.dagger.components.ApplicationComponent;
import org.openstack.android.openstacksummit.dagger.components.DaggerApplicationComponent;
import org.openstack.android.openstacksummit.dagger.modules.ApplicationModule;

/**
 * Created by claudio on 11/3/2015.
 */
public class OpenStackSummitApplication extends Application {
    private ApplicationComponent applicationComponent;

    @Override public void onCreate() {
        super.onCreate();
        this.initializeInjector();
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }
}
