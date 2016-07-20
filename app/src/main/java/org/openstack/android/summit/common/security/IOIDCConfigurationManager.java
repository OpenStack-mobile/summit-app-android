package org.openstack.android.summit.common.security;

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

    IdentityProviderUrls buildIdentityProviderUrls();

}
