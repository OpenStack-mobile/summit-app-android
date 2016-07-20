package org.openstack.android.summit.common.security.oidc;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import org.openstack.android.summit.common.security.IdentityProviderUrls;
import org.openstack.android.summit.common.security.OIDCNativeClientConfiguration;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sebastian on 7/20/2016.
 */
final public class CodeFlowAuthUrlBuilderStrategy implements IAuthUrlBuilderStrategy {
    /**
     * Generates an Authentication Request URL to the Authorization Endpoint to start an Code Flow.
     * When using the Code Flow, all tokens are returned from the Token Endpoint.
     * The Authorization Server can authenticate the Client before exchanging the Authorization Code
     * for an Access Token.
     * @see <a href="http://openid.net/specs/openid-connect-core-1_0.html#CodeFlowAuth">Code Flow</a>
     */
    @Override
    public String build(OIDCNativeClientConfiguration clientConfiguration, IdentityProviderUrls identityProviderUrls) {
        List<String> scopesList = Arrays.asList(clientConfiguration.getScopes());

        AuthorizationCodeRequestUrl request = new AuthorizationCodeRequestUrl(identityProviderUrls.getAuthorizationEndpoint(), clientConfiguration.getClientId())
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
