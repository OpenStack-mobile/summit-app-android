package org.openstack.android.summit.common.security.oidc;

/**
 * Created by sebastian on 7/20/2016.
 */
public final class AuthCodeResponse {

    OIDCNativeClientConfiguration clientConfiguration;
    String authCode;
    String state;

    /**
     *
     * @param clientConfiguration
     * @param authCode
     * @param state
     */
    public AuthCodeResponse(OIDCClientConfiguration clientConfiguration, String authCode, String state ){
        this.clientConfiguration = (OIDCNativeClientConfiguration)clientConfiguration;
        this.authCode = authCode;
        this.state = state;
    }

    /**
     *
     * @param clientConfiguration
     * @param authCode
     */
    public AuthCodeResponse(OIDCClientConfiguration clientConfiguration, String authCode){
        this.clientConfiguration = (OIDCNativeClientConfiguration)clientConfiguration;
        this.authCode = authCode;
    }

    public OIDCNativeClientConfiguration getClientConfiguration() {
        return clientConfiguration;
    }

    public String getAuthCode() {
        return authCode;
    }

    public String getState() {
        return state;
    }

    public String getReturnUrl(){ return clientConfiguration.getReturnUrl(); }

    public String getClientId(){ return clientConfiguration.getClientId(); }

    public String getClientSecret(){ return clientConfiguration.getClientSecret(); }
}
