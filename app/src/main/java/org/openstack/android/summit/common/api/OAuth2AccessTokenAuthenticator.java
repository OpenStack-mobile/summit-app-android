package org.openstack.android.summit.common.api;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.security.ITokenManager;

import java.io.IOException;
import java.security.InvalidParameterException;

import javax.inject.Inject;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by smarcet on 11/8/16.
 */

public class OAuth2AccessTokenAuthenticator implements Authenticator {

    private ITokenManager tokenManager;
    private IOAuth2AccessTokenSendStrategy accessTokenSendStrategy;

    @Inject
    public OAuth2AccessTokenAuthenticator
    (
        ITokenManager tokenManager,
        IOAuth2AccessTokenSendStrategy accessTokenSendStrategy
    )
    {
        this.tokenManager            = tokenManager;
        this.accessTokenSendStrategy = accessTokenSendStrategy;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        // Refresh your access_token using a synchronous api request

        try {

            String accessToken = tokenManager.getToken();

            if(accessToken != null)
                tokenManager.invalidateToken(accessToken);

            accessToken = tokenManager.getToken();

            if(accessToken == null || accessToken.isEmpty())
                throw new InvalidParameterException("Missing OAUTH2 Access Token");

            return accessTokenSendStrategy.process(response.request(), accessToken);

        }
        catch (Exception ex){
            Crashlytics.logException(ex);
            throw new IOException();
        }
    }
}
