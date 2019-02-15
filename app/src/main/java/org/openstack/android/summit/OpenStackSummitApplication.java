package org.openstack.android.summit;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import net.danlew.android.joda.JodaTimeAndroid;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.user_interface.AlertsBuilder;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.dagger.components.ApplicationComponent;
import org.openstack.android.summit.dagger.modules.ApplicationModule;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import org.openstack.android.summit.dagger.components.DaggerApplicationComponent;
/**
 * Created by claudio on 11/3/2015.
 */
public class OpenStackSummitApplication extends Application {

    private ApplicationComponent applicationComponent;
    public static Context        context;
    
    @Override public void onCreate() {
        super.onCreate();

        JodaTimeAndroid.init(this);

        // Set up Crashlytics, disabled for debug builds
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();

        // Initialize Fabric with the debug-disabled crashlytics.
        Fabric.with(this, crashlyticsKit);

        this.initializeInjector();

        context = getApplicationContext();

        Fresco.initialize(context);
        RealmConfiguration realmConfiguration = null;

        try {
            realmConfiguration = RealmFactory.buildDefaultConfiguration(context);
            Realm.setDefaultConfiguration(realmConfiguration);
            // try to compact file
            Realm.compactRealm(realmConfiguration);
        }
        catch(Exception ex){
            Crashlytics.logException(ex);
            if(realmConfiguration != null) {
                // if error delete the current realm
                Realm.deleteRealm(realmConfiguration);
                Realm.setDefaultConfiguration(realmConfiguration);
            }
        }

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
