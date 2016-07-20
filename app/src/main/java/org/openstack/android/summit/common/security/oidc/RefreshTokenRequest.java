package org.openstack.android.summit.common.security.oidc;

import org.openstack.android.summit.common.security.OIDCNativeClientConfiguration;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sebastian on 7/20/2016.
 */
final public class RefreshTokenRequest {

    OIDCNativeClientConfiguration clientConfiguration;
    String refreshToken;

    public OIDCNativeClientConfiguration getClientConfiguration() {
        return clientConfiguration;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public RefreshTokenRequest(OIDCNativeClientConfiguration clientConfiguration, String refreshToken) {

        this.clientConfiguration = clientConfiguration;
        this.refreshToken = refreshToken;
    }

    public String getClientId(){ return clientConfiguration.getClientId(); }

    public String getClientSecret(){ return clientConfiguration.getClientSecret(); }

    public List<String> getScopes(){
        return Arrays.asList(this.clientConfiguration.getScopes());
    }
}
