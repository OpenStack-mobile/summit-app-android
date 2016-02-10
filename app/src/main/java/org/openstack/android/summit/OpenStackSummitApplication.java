package org.openstack.android.summit;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.openstack.android.summit.dagger.components.ApplicationComponent;
import org.openstack.android.summit.dagger.components.DaggerApplicationComponent;
import org.openstack.android.summit.dagger.modules.ApplicationModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by claudio on 11/3/2015.
 */
public class OpenStackSummitApplication extends Application {
    private ApplicationComponent applicationComponent;
    public static Context context;
    
    @Override public void onCreate() {
        super.onCreate();
        this.initializeInjector();
        RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext()).build();
        Realm.setDefaultConfiguration(config);

        context = getApplicationContext();

        Fresco.initialize(context);
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
