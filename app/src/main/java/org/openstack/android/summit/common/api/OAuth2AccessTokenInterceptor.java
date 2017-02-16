package org.openstack.android.summit.common.api;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.security.ITokenManager;

import java.io.IOException;
import java.security.InvalidParameterException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by smarcet on 11/8/16.
 */

public class OAuth2AccessTokenInterceptor implements Interceptor {

    private ITokenManager tokenManager;
    private IOAuth2AccessTokenSendStrategy accessTokenSendStrategy;

    @Inject
    public OAuth2AccessTokenInterceptor
    (
        ITokenManager tokenManager,
        IOAuth2AccessTokenSendStrategy accessTokenSendStrategy
    )
    {
        this.tokenManager            = tokenManager;
        this.accessTokenSendStrategy = accessTokenSendStrategy;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            String accessToken      = tokenManager.getToken();

            if(accessToken == null || accessToken.isEmpty())
                throw new InvalidParameterException("Missing OAUTH2 Access Token");

            return chain.proceed(accessTokenSendStrategy.process(chain.request(), accessToken));
        }
        catch (Exception ex){
            Crashlytics.logException(ex);
            Log.d(Constants.LOG_TAG,ex.getMessage(), ex);
            throw new IOException("invalid access token!.");
        }
    }
}
