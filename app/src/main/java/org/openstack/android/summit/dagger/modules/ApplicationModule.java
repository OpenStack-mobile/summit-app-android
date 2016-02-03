package org.openstack.android.summit.dagger.modules;

import android.content.Context;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.ScheduleFilter;
import org.openstack.android.summit.common.Session;

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
    Context providesApplicationContext() {
        return this.application;
    }

    @Provides
    @Singleton
    ISession providesSession() {
        return new Session();
    }

    @Provides
    @Singleton
    IScheduleFilter providesScheduleFilter() {
        return new ScheduleFilter();
    }
}
