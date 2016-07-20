package org.openstack.android.summit.common.security.oidc;

import org.openstack.android.summit.common.security.IdentityProviderUrls;
import org.openstack.android.summit.common.security.OIDCNativeClientConfiguration;

/**
 * Created by sebastian on 7/20/2016.
 */
public interface IAuthUrlBuilderStrategy {

    /**
     * build the auth url for depending on clientConfiguration
     * @param clientConfiguration
     * @param identityProviderUrls
     * @return
     */
    public String build(OIDCNativeClientConfiguration clientConfiguration, IdentityProviderUrls identityProviderUrls);
}
