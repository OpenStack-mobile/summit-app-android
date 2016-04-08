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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Claudio Redi on 12/8/2015.
 */
public class TokenManagerServiceAccount implements ITokenManager {
    public final String TOKEN_SERVICE_ACCOUNT = "token_service_account";
    private IDecoder decoder = new Decoder();

    @Override
    public String getToken() throws TokenGenerationException {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(OpenStackSummitApplication.context);
        String token = settings.getString(TOKEN_SERVICE_ACCOUNT, "");

        if (token == "" || token == null) {

            TokenResponse tokenResponse = null;
            try {
                tokenResponse = getOauth2AccessToken(decoder.getTokenServerUrl(), decoder.getClientIDServiceAccount(), decoder.getClientSecretServiceAccount());
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
        tokenRequest.setScopes(new ArrayList<>(Arrays.asList(decoder.getScopesServiceAccount())));
        TokenResponse tokenResponse = tokenRequest.execute();

        return tokenResponse;
    }
}
