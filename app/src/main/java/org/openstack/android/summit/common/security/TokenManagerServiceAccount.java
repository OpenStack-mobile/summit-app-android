package org.openstack.android.summit.common.security;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.api.client.auth.oauth2.TokenResponse;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.security.oidc.AccessTokenRequest;
import org.openstack.android.summit.common.security.oidc.IOIDCConfigurationManager;
import org.openstack.android.summit.common.security.oidc.OIDCClientConfiguration;
import org.openstack.android.summit.common.security.oidc.OpenIdConnectProtocol;

import java.io.IOException;

/**
 * Created by Claudio Redi on 12/8/2015.
 */
public class TokenManagerServiceAccount implements ITokenManager {

    public final String TOKEN_SERVICE_ACCOUNT = "token_service_account";
    private OIDCClientConfiguration clientConfig;
    private OpenIdConnectProtocol oidcProtocol;

    public TokenManagerServiceAccount(IOIDCConfigurationManager oidcConfigurationManager){
        clientConfig  = oidcConfigurationManager.buildConfiguration(OIDCClientConfiguration.ODICAccountType.ServiceAccount);
        oidcProtocol  = new OpenIdConnectProtocol(oidcConfigurationManager.buildIdentityProviderUrls());
    }

    @Override
    public String getToken() throws TokenGenerationException {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(OpenStackSummitApplication.context);
        String token               = settings.getString(TOKEN_SERVICE_ACCOUNT, "");

        if (token == null || token.isEmpty()) {

            TokenResponse tokenResponse = null;
            try {
                tokenResponse = oidcProtocol.makeAccessTokenRequest(new AccessTokenRequest(clientConfig));
            } catch (IOException e) {
                throw new TokenGenerationException(e);
            }
            SharedPreferences.Editor editor = settings.edit();
            token                           = tokenResponse.getAccessToken();
            if(token == null || token.isEmpty())
                throw new TokenGenerationException("token is null from token response!");
            editor.putString(TOKEN_SERVICE_ACCOUNT, token);
            editor.apply();
        }

        return token;
    }

    @Override
    public void invalidateToken(String token) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(OpenStackSummitApplication.context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(TOKEN_SERVICE_ACCOUNT, null);
        editor.apply();
    }
}
