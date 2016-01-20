package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.NavigationParametersStore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 1/19/2016.
 */
@Module
public class BaseModule {
    @Singleton
    @Provides
    INavigationParametersStore providesNavigationParametersStore() {
        return new NavigationParametersStore();
    }
}
