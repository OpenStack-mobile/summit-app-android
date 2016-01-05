package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.data_access.IMemberDataStore;
import org.openstack.android.summit.common.network.HttpFactory;
import org.openstack.android.summit.common.network.HttpTaskFactory;
import org.openstack.android.summit.common.network.IHttpFactory;
import org.openstack.android.summit.common.network.IHttpTaskFactory;
import org.openstack.android.summit.common.security.*;
import org.openstack.android.summit.common.security.SecurityManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 12/15/2015.
 */
@Module
public class SecurityModule {
    @Provides
    IHttpFactory providesHttpFactory() {
        return new HttpFactory();
    }

    @Provides
    ITokenManagerFactory providesTokenManagerFactory() {
        return new TokenManagerFactory(new TokenManagerOIDC(), new TokenManagerServiceAccount());
    }

    @Provides
    IHttpTaskFactory providesHttpTaskFactory(ITokenManagerFactory tokenManagerFactory, IHttpFactory httpFactory) {
        return new HttpTaskFactory(tokenManagerFactory, httpFactory);
    }

    @Provides
    @Singleton
    ISecurityManager providesSecurityManager(IHttpTaskFactory httpTaskFactory, IMemberDataStore memberDataStore, ISession session) {
        return new SecurityManager(httpTaskFactory, memberDataStore, session);
    }
}
