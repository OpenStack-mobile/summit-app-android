package org.openstack.android.summit.common.security;

/**
 * Created by sebastian on 7/18/2016.
 */
final public class IdentityProviderUrls {

    private String baseUrl;

    static private final String tokenEndpointUrl = "/oauth2/token";

    static private final String AuthorizationEndpointUrl = "/oauth2/auth";

    static private final  String UserInfoEndpointUrl = "/api/v1/users/me";

    public IdentityProviderUrls(String baseUrl){
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl(){
        return this.baseUrl;
    }

    public String getTokenEndpoint(){
        return this.baseUrl+IdentityProviderUrls.tokenEndpointUrl;
    }

    public String getAuthorizationEndpoint(){
        return this.baseUrl+IdentityProviderUrls.AuthorizationEndpointUrl;
    }

    public String getUserInfoEndpoint(){
        return this.baseUrl+IdentityProviderUrls.UserInfoEndpointUrl;
    }
}
