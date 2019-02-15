package org.openstack.android.summit.common.security.oidc;

import org.openstack.android.summit.common.security.IdentityProviderUrls;
import org.openstack.android.summit.common.security.oidc.OIDCClientConfiguration;

/**
 * Created by sebastian on 7/18/2016.
 */
public interface IOIDCConfigurationManager {

    /**
     * create a OIDC configuration based on specify client
     * @param accountType
     * @return
     */
    OIDCClientConfiguration buildConfiguration(OIDCClientConfiguration.ODICAccountType accountType);

    String getResourceServerBaseUrl();

    String getResourceServerBaseRealmUrl();

    IdentityProviderUrls buildIdentityProviderUrls();

}
