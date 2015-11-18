package org.openstack.android.openstacksummit;

import android.app.Application;

import org.openstack.android.openstacksummit.dagger.components.ApplicationComponent;
import org.openstack.android.openstacksummit.dagger.components.DaggerApplicationComponent;
import org.openstack.android.openstacksummit.dagger.modules.ApplicationModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by claudio on 11/3/2015.
 */
public class OpenStackSummitApplication extends Application {
    private ApplicationComponent applicationComponent;

    @Override public void onCreate() {
        super.onCreate();
        this.initializeInjector();
        RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext()).build();
        Realm.setDefaultConfiguration(config);
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
