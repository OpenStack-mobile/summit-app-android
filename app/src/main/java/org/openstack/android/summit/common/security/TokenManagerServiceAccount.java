package org.openstack.android.summit.common.security;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.api.client.auth.oauth2.TokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.gson.GsonFactory;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Claudio Redi on 12/8/2015.
 */
public class TokenManagerServiceAccount implements ITokenManager {
    public final String TOKEN_SERVICE_ACCOUNT = "token_service_account";

    @Override
    public String getToken() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(OpenStackSummitApplication.context);
        String token = settings.getString(TOKEN_SERVICE_ACCOUNT, "");

        if (token == "" || token == null) {
            TokenResponse tokenResponse = getOauth2AccessToken(Constants.TOKEN_SERVER_URL, ConfigServiceAccount.clientId, ConfigServiceAccount.clientSecret);
            SharedPreferences.Editor editor = settings.edit();
            token = tokenResponse.getAccessToken();
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

    private TokenResponse getOauth2AccessToken(String accessTokenURL, String clientId, String clientSecret) {
        String accessTokenURLWithClientCredentialsGrantTypeQueryParam = accessTokenURL + "?grant_type=client_credentials";

        TokenRequest tokenRequest = new TokenRequest(
                AndroidHttp.newCompatibleTransport(),
                new GsonFactory(),
                new GenericUrl(accessTokenURLWithClientCredentialsGrantTypeQueryParam),
                "client_credentials");
        tokenRequest.setClientAuthentication(new BasicAuthentication(clientId, clientSecret));
        tokenRequest.setScopes(new ArrayList<>(Arrays.asList(ConfigOIDC.scopes)));
        TokenResponse tokenResponse = null;
        try {
            tokenResponse = tokenRequest.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tokenResponse;
    }
}
