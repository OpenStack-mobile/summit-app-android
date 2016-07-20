package org.openstack.android.summit.dagger.modules;

import android.content.Context;

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
    ITokenManagerFactory providesTokenManagerFactory(IOIDCConfigurationManager oidcConfigurationManager) {
        return new TokenManagerFactory(new TokenManagerOIDC(), new TokenManagerServiceAccount(oidcConfigurationManager));
    }

    @Provides
    IHttpTaskFactory providesHttpTaskFactory(ITokenManagerFactory tokenManagerFactory, IHttpFactory httpFactory) {
        return new HttpTaskFactory(tokenManagerFactory, httpFactory);
    }

    @Provides
    @Singleton
    ISecurityManager providesSecurityManager(IMemberDataStore memberDataStore, ISession session) {
        return new SecurityManager(new TokenManagerOIDC(), memberDataStore, session);
    }

    @Provides
    @Singleton
    IOIDCConfigurationManager providesOIDCConfigurationManager(Context context) {
        return new OIDCConfigurationManager(new Base64Decoder(), new IConfigurationParamFinderStrategy[] {
                new ConfigurationParamMetadataFinderStrategy(context)
        });
    }
}
