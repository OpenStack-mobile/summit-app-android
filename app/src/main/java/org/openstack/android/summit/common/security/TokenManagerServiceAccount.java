package org.openstack.android.summit.common.security;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.api.client.auth.oauth2.TokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.gson.GsonFactory;

import org.openstack.android.summit.BuildConfig;
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
    public String getToken() throws TokenGenerationException {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(OpenStackSummitApplication.context);
        String token = settings.getString(TOKEN_SERVICE_ACCOUNT, "");

        if (token == "" || token == null) {

            TokenResponse tokenResponse = null;
            try {
                tokenResponse = getOauth2AccessToken(getTokenServerUrl(), getClientID(), getClientSecret());
            } catch (IOException e) {
                throw new TokenGenerationException(e);
            }
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

    private TokenResponse getOauth2AccessToken(String accessTokenURL, String clientId, String clientSecret) throws IOException {
        String accessTokenURLWithClientCredentialsGrantTypeQueryParam = accessTokenURL + "?grant_type=client_credentials";

        TokenRequest tokenRequest = new TokenRequest(
                AndroidHttp.newCompatibleTransport(),
                new GsonFactory(),
                new GenericUrl(accessTokenURLWithClientCredentialsGrantTypeQueryParam),
                "client_credentials");
        tokenRequest.setClientAuthentication(new BasicAuthentication(clientId, clientSecret));
        tokenRequest.setScopes(new ArrayList<>(Arrays.asList(getScopes())));
        TokenResponse tokenResponse = tokenRequest.execute();

        return tokenResponse;
    }

    private String getClientID() {
        String value = "";
        if (BuildConfig.DEBUG) {
            value = Constants.ConfigServiceAccount.TEST_CLIENT_ID;
        }
        else {
            value = Constants.ConfigServiceAccount.PRODUCTION_CLIENT_ID;
        }
        return value;
    }

    private String getClientSecret() {
        String value = "";
        if (BuildConfig.DEBUG) {
            value = Constants.ConfigServiceAccount.TEST_CLIENT_SECRET;
        }
        else {
            value = Constants.ConfigServiceAccount.PRODUCTION_CLIENT_SECRET;
        }
        return value;
    }

    private String getTokenServerUrl() {
        String value = "";
        if (BuildConfig.DEBUG) {
            value = Constants.TEST_TOKEN_SERVER_URL;
        }
        else {
            value = Constants.PRODUCTION_TOKEN_SERVER_URL;
        }
        return value;
    }

    private String[] getScopes() {
        String[] value = null;
        if (BuildConfig.DEBUG) {
            value = Constants.ConfigServiceAccount.TEST_SCOPES;
        }
        else {
            value = Constants.ConfigServiceAccount.PRODUCTION_SCOPES;
        }
        return value;
    }
}
