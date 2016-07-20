package org.openstack.android.summit.common.security;

/**
 * Created by sebastian on 7/18/2016.
 */
public class OIDCServiceClientConfiguration extends OIDCClientConfiguration {
    /**
     * @param clientId
     * @param clientSecret
     * @param scopes
     */
    public OIDCServiceClientConfiguration(String clientId, String clientSecret, String[] scopes) {
        super(clientId, clientSecret, scopes);
    }

    @Override
    public ODICAccountType getType() {
        return ODICAccountType.ServiceAccount;
    }
}
