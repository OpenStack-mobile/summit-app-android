package org.openstack.android.summit.common.security;

/**
 * Created by sebastian on 7/18/2016.
 */
abstract public class OIDCClientConfiguration {

    protected OIDCClientConfiguration() {
    }

    public enum ODICAccountType {
        ServiceAccount,
        NativeAccount
    };

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String[] getScopes() {
        return scopes;
    }

    protected String clientId;

    protected String clientSecret;

    protected String[] scopes;

    abstract public ODICAccountType getType();

    /**
     *
     * @param clientId
     * @param clientSecret
     * @param scopes
     */
    public OIDCClientConfiguration(String clientId, String clientSecret, String[] scopes) {
        this.clientId     = clientId;
        this.clientSecret = clientSecret;
        this.scopes       = scopes;
    }
}
