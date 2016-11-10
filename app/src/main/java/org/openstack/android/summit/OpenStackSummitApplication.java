package org.openstack.android.summit;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import net.danlew.android.joda.JodaTimeAndroid;

import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.dagger.components.ApplicationComponent;
import org.openstack.android.summit.dagger.components.DaggerApplicationComponent;
import org.openstack.android.summit.dagger.modules.ApplicationModule;
import org.openstack.android.summit.dagger.modules.RestApiModule;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;

/**
 * Created by claudio on 11/3/2015.
 */
public class OpenStackSummitApplication extends Application {

    private ApplicationComponent applicationComponent;
    public static Context        context;
    
    @Override public void onCreate() {
        super.onCreate();

        JodaTimeAndroid.init(this);

        Fabric.with(
                this,
                new Crashlytics.Builder()
                        .core(new CrashlyticsCore.Builder()
                                .disabled(BuildConfig.DEBUG)
                                .build())
                        .build()
        );

        this.initializeInjector();

        Realm.setDefaultConfiguration(RealmFactory.buildDefaultConfiguration(getApplicationContext()));

        context = getApplicationContext();

        Fresco.initialize(context);

        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        if(BuildConfig.DEBUG) {
            // enable Stetho (http://facebook.github.io/stetho) and realm plugin
            // chrome://inspect/#devices
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                            .build());
            //LeakCanary.install(this);
        }
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
