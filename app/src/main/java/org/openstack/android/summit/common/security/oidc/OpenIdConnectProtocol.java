package org.openstack.android.summit.common.security.oidc;

import android.text.TextUtils;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.auth.openidconnect.IdTokenResponse;
import com.google.api.client.auth.openidconnect.IdTokenVerifier;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.gson.GsonFactory;
import org.openstack.android.summit.common.security.IdentityProviderUrls;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sebastian on 7/20/2016.
 */
public final class OpenIdConnectProtocol {

    /**
     * Created by sebastian on 7/20/2016.
     */
    final static private class AuthUrlBuilderStrategyFactory {

        /**
         *
         * @param clientConfiguration
         * @return
         */
        public static IAuthUrlBuilderStrategy build(OIDCNativeClientConfiguration clientConfiguration){
            switch (clientConfiguration.getFlowType()){
                case AuthorizationCode:
                    return new CodeFlowAuthUrlBuilderStrategy();
                case Hybrid:
                    return new HybridFlowAuthUrlBuilderStrategy();
                case Implicit:
                    return new ImplicitFlowAuthUrlBuilderStrategy();
                default:
                    return new CodeFlowAuthUrlBuilderStrategy();
            }
        }
    }

    private IdentityProviderUrls identityProviderUrls;

    public OpenIdConnectProtocol(IdentityProviderUrls identityProviderUrls){
        this.identityProviderUrls = identityProviderUrls;
    }

    public AuthCodeRequest buildAuthRequest(OIDCNativeClientConfiguration clientConfiguration){
        IAuthUrlBuilderStrategy strategy = AuthUrlBuilderStrategyFactory.build(clientConfiguration);
        String url                       = strategy.build(clientConfiguration, this.identityProviderUrls);
        return new AuthCodeRequest(url);
    }

    public IdTokenResponse makeTokenRequest(AuthCodeResponse response) throws IOException{

        AuthorizationCodeTokenRequest tokenRequest = new AuthorizationCodeTokenRequest(
                AndroidHttp.newCompatibleTransport(),
                new GsonFactory(),
                new GenericUrl(identityProviderUrls.getTokenEndpoint()),
                response.getAuthCode()
        );
        tokenRequest.set("redirect_uri", response.getReturnUrl());

        if (!TextUtils.isEmpty(response.getClientSecret())) {
            tokenRequest.setClientAuthentication(new BasicAuthentication(response.getClientId(), response.getClientSecret()));
        }

        IdTokenResponse tokenResponse = IdTokenResponse.execute(tokenRequest);
        String idToken = tokenResponse.getIdToken();

        if (isValidIdToken(response.getClientId(), idToken))
            return tokenResponse;
        throw new IOException("Invalid ID token returned.");
    }


    public IdTokenResponse makeRefreshTokenRequest(RefreshTokenRequest request) throws IOException{

        com.google.api.client.auth.oauth2.RefreshTokenRequest refreshTokenRequest = new com.google.api.client.auth.oauth2.RefreshTokenRequest(
                AndroidHttp.newCompatibleTransport(),
                new GsonFactory(),
                new GenericUrl(identityProviderUrls.getTokenEndpoint()),
                request.getRefreshToken()
        );

        if (!TextUtils.isEmpty(request.getClientSecret())) {
            refreshTokenRequest.setClientAuthentication(new BasicAuthentication(request.getClientId(), request.getClientSecret()));
        }
        refreshTokenRequest.setScopes(request.getScopes());

        return IdTokenResponse.execute(refreshTokenRequest);
    }

    /**
     * @see <a href="https://tools.ietf.org/html/rfc6749#section-4.4">Client Credentials Grant</a>
     * @param request
     * @return
     * @throws IOException
     */
    public TokenResponse makeAccessTokenRequest(AccessTokenRequest request) throws IOException{

        TokenRequest tokenRequest = new TokenRequest(
                AndroidHttp.newCompatibleTransport(),
                new GsonFactory(),
                new GenericUrl(this.identityProviderUrls.getTokenEndpoint()),
                request.getGrantType());

        tokenRequest.setClientAuthentication(new BasicAuthentication(request.getClientId(), request.getClientSecret()))
                    .setScopes(request.getScopes());

        return tokenRequest.execute();
    }

    /**
     * Verifies an ID Token.
     * TODO: Look into verifying the token issuer as well?
     */
    public static boolean isValidIdToken(String clientId, String tokenString) throws IOException {

        List<String> audiences = Arrays.asList(clientId);
        IdTokenVerifier verifier = new IdTokenVerifier.Builder().setAudience(audiences).build();

        IdToken idToken = IdToken.parse(new GsonFactory(), tokenString);

        return verifier.verify(idToken);
    }
}
