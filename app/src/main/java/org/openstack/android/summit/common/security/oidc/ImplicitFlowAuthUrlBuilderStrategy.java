package org.openstack.android.summit.common.security.oidc;

import com.google.api.client.auth.oauth2.AuthorizationRequestUrl;
import org.openstack.android.summit.common.security.IdentityProviderUrls;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sebastian on 7/20/2016.
 */
final public class ImplicitFlowAuthUrlBuilderStrategy implements IAuthUrlBuilderStrategy {

    @Override
    /**
     * Generates an Authentication Request URL to the Authorization Endpoint to start an Implicit Flow.
     * When using the Implicit Flow, all tokens are returned from the Authorization Endpoint; the
     * Token Endpoint is not used so it allows to get all tokens on one trip. The downside is that
     * it doesn't support refresh tokens.
     * @see <a href="http://openid.net/specs/openid-connect-core-1_0.html#ImplicitFlowAuth">Implicit Flow</a>
     */
    public String build(OIDCNativeClientConfiguration clientConfiguration, IdentityProviderUrls identityProviderUrls) {
        //TODO: see what the following statement implies :
        // "While OAuth 2.0 also defines the token Response Type value for the Implicit Flow,
        // OpenID Connect does not use this Response Type, since no ID Token would be returned"
        // from http://openid.net/specs/openid-connect-core-1_0.html#Authentication
        String[] responsesTypes = {"id_token", "token"};
        List<String> scopesList = Arrays.asList(clientConfiguration.getScopes());
        List<String> responsesList = Arrays.asList(responsesTypes);

        //REQUIRED  OIDC request params
        AuthorizationRequestUrl request = new AuthorizationRequestUrl(identityProviderUrls.getAuthorizationEndpoint(),
                clientConfiguration.getClientId(),
                responsesList)
                .setRedirectUri(clientConfiguration.getReturnUrl())
                .setScopes(scopesList);

        //OPTIONAL OIDC request params
        if (scopesList.contains("offline_access")) {
            // If the list of scopes includes the special `offline_access` scope that enables issuing
            // of Refresh Tokens, we need to ask for consent by including this parameter.
            request.set("prompt", "login+consent");
        } else {
            // Tell the server to ask for login details again. This ensures that in case of multiple
            // accounts, the user won't accidentally authorise the wrong one.
            request.set("prompt", "login");
        }

        // An optional request parameter that asks the server to provide a touch-enabled interface.
        // Who knows, maybe the server is nice enough to make some changes.
        //request.set("display", "touch");

        return request.build();
    }
}
