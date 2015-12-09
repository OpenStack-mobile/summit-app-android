package org.openstack.android.summit.dagger.modules;

import android.content.Context;

import org.openstack.android.summit.OpenStackSummitApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by claudio on 11/3/2015.
 */
@Module
public class ApplicationModule {
    private final OpenStackSummitApplication application;

    public ApplicationModule(OpenStackSummitApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return this.application;
    }

}
