package org.openstack.android.summit.common.security.oidc;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sebastian on 7/20/2016.
 */
final public class AccessTokenRequest {

    private OIDCClientConfiguration clientConfiguration;

    public AccessTokenRequest(OIDCClientConfiguration clientConfiguration){
        this.clientConfiguration = clientConfiguration;
    }

    public String getGrantType() {
        return "client_credentials";
    }

    public List<String> getScopes(){
        return Arrays.asList(this.clientConfiguration.getScopes());
    }

    public String getClientId(){
        return this.clientConfiguration.getClientId();
    }

    public String getClientSecret(){
        return this.clientConfiguration.getClientSecret();
    }
}
