package org.openstack.android.summit.common.security.oidc;

import com.google.api.client.auth.oauth2.AuthorizationRequestUrl;
import org.openstack.android.summit.common.security.IdentityProviderUrls;
import org.openstack.android.summit.common.security.OIDCNativeClientConfiguration;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sebastian on 7/20/2016.
 */
final public class HybridFlowAuthUrlBuilderStrategy implements IAuthUrlBuilderStrategy {

    /**
     * Generates an Authentication Request URL to the Authorization Endpoint to start an Hybrid Flow.
     * When using the Hybrid Flow, some tokens are returned from the Authorization Endpoint and
     * others are returned from the Token Endpoint.
     * @see <a href="http://openid.net/specs/openid-connect-core-1_0.html#HybridFlowAuth">Hybrid Flow</a>
     */
    @Override
    public String build(OIDCNativeClientConfiguration clientConfiguration, IdentityProviderUrls identityProviderUrls) {
        // The response type "code" is the only mandatory response type on hybrid flow, it must be
        // coupled with other response types to form one of the following values : "code id_token",
        // "code token", or "code id_token token".
        // For our needs "token" is not defined here because we want an access_token that has made
        // a client authentication. That access_token will be retrieve later using the TokenEndpoint
        // (see #requestTokens).
        String[] responsesTypes = {"code", "id_token"};
        List<String> scopesList = Arrays.asList(clientConfiguration.getScopes());
        List<String> responsesList = Arrays.asList(responsesTypes);

        //REQUIRED  OIDC request params
        AuthorizationRequestUrl request = new AuthorizationRequestUrl(identityProviderUrls.getAuthorizationEndpoint(), clientConfiguration.getClientId(), responsesList)
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
