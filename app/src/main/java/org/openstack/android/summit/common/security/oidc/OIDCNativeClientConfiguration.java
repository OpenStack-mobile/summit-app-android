package org.openstack.android.summit.common.security.oidc;

/**
 * Created by sebastian on 7/18/2016.
 */
public class OIDCNativeClientConfiguration extends OIDCClientConfiguration {

    public OIDCNativeClientConfiguration(String clientId, String clientSecret, String returnUrl, String[] scopes) {
        super(clientId, clientSecret, scopes);
        this.returnUrl = returnUrl;
    }

    @Override
    public ODICAccountType getType() {
        return ODICAccountType.NativeAccount;
    }

    private String returnUrl;

    public String getReturnUrl(){
        return returnUrl;
    }

    public enum Flows
    {
        AuthorizationCode,  //http://openid.net/specs/openid-connect-core-1_0.html#CodeFlowAuth
        Implicit,           //http://openid.net/specs/openid-connect-core-1_0.html#ImplicitFlowAuth
        Hybrid              //http://openid.net/specs/openid-connect-core-1_0.html#HybridFlowAuth
    }


    public Flows getFlowType(){
        return Flows.AuthorizationCode;
    }
}
