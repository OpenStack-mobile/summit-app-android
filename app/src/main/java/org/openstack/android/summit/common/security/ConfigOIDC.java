package org.openstack.android.summit.common.security;

/**
 * Simple utility class for storing OpenID Connect configuration. This should not be used in
 * production. If you want to hide your keys, you should obfuscate them using ProGuard (with added
 * manual obfuscation), DexGuard or something else.
 *
 * See this Stack Overflow post for some suggestions:
 * https://stackoverflow.com/a/14570989
 */
public final class ConfigOIDC {
    public static final String TEST_CLIENT_ID = "9HKaikkxU~6Qvor_GvhQm6ADYi7QmwUL.openstack.client";
    public static final String TEST_CLIENT_SECRET = "oWqW0hf49Im5Iyk-z59DsOekc9FZbcfVc4o9sGGX6gw3Ef6NRntlhqWEwzDGYPfT";
    public static final String PRODUCTION_CLIENT_ID = "Gg2_KiXCd3OO1f8Gas_cy7RSR1g8EApY.openstack.client";
    public static final String PRODUCTION_CLIENT_SECRET = "JgcmXHFR7s88KWd1J5hTL9Z70mSs2IAo8NnHTWCOsz.N16hMxXUUCXur6v3gg9Ma";

    // This URL doesn't really have a use with native apps and basically just signifies the end
    // of the authorisation process. It doesn't have to be a real URL, but it does have to be the
    // same URL that is registered with your provider.
    public static final String REDIRECT_URL = "org.openstack.android.summit://oauthCallback";

    // The `offline_access` scope enables us to request Refresh Tokens, so we don't have to ask the
    // user to authorise us again every time the tokens expire. Some providers might have an
    // `offline` scope instead. If you get an `invalid_scope` error when trying to authorise the
    // app, try changing it to `offline`.
    public static final String[] SCOPES = {"openid", "offline_access", "https://testresource-server.openstack.org/summits/read", "https://testresource-server.openstack.org/summits/write"};

    public enum Flows
    {
        AuthorizationCode,  //http://openid.net/specs/openid-connect-core-1_0.html#CodeFlowAuth
        Implicit,           //http://openid.net/specs/openid-connect-core-1_0.html#ImplicitFlowAuth
        Hybrid              //http://openid.net/specs/openid-connect-core-1_0.html#HybridFlowAuth
    }

    // The authorization flow type that determine the response_type authorization request should use.
    // One of the supported flows AuthorizationCode, Implicit or Hybrid.
    // For more info see http://openid.net/specs/openid-connect-core-1_0.html#Authentication
    public static final Flows FLOW_TYPE = Flows.AuthorizationCode;

}