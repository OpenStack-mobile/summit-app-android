package org.openstack.android.summit.common;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public class Constants {
    static public final String LOG_TAG = "OpenStackSummit";
    static public final String TEST_TOKEN_SERVER_URL = "https://testopenstackid.openstack.org/oauth2/token";
    static public final String TEST_AUTHORIZATION_SERVER_URL = "https://testopenstackid.openstack.org/oauth2/auth";
    static public final String TEST_RESOURCE_SERVER_BASE_URL = "https://testresource-server.openstack.org";
    static public final String TEST_USER_INFO_SERVER_URL = "https://testopenstackid.openstack.org/oauth2/userinfo";
    static public final String PRODUCTION_TOKEN_SERVER_URL = "https://openstackid.org/oauth2/token";
    static public final String PRODUCTION_AUTHORIZATION_SERVER_URL = "https://openstackid.org/oauth2/auth";
    static public final String PRODUCTION_RESOURCE_SERVER_BASE_URL = "https://openstackid-resources.openstack.org";
    static public final String PRODUCTION_USER_INFO_SERVER_URL = "https://openstackid.org/oauth2/userinfo";

    static public final String CURRENT_MEMBER_ID = "CurrentMemberId";
    static public final String LOGGED_IN_EVENT = "logged-in-event";
    static public final String LOGGED_OUT_EVENT = "logged-out-event";
    static public final String NAVIGATION_PARAMETER_SEARCH_TERM = "SearchTerm";
    static public final String NAVIGATION_PARAMETER_EVENT_ID = "EventId";
    public static final String NAVIGATION_PARAMETER_LEVEL = "Level";
    public static final String NAVIGATION_PARAMETER_TRACK = "Track";
    public static final String NAVIGATION_PARAMETER_IS_MY_PROFILE = "IsMyProfile";
    public static final String NAVIGATION_PARAMETER_SPEAKER = "SpeakerId";
    public static final String NAVIGATION_PARAMETER_VENUE = "VenueId";

    public static class ConfigServiceAccount {
        // TODO: Add the information you received from your OIDC provider below.
        public static final String TEST_CLIENT_ID = "JYvz.AKTzlpLfSl4G.i42Abp-e5dVGVn.openstack.client";
        public static final String TEST_CLIENT_SECRET = "lQxHMrd576TK.iLXFXVK7mpxY1hOie~gD71T_2H1o7kR0GlstSeomMU~rfMfy_Vy";
        public static final String PRODUCTION_CLIENT_ID = "h70UpXbSlOAx-i.pYxPDM7qE03FW2nWF.openstack.client";
        public static final String PRODUCTION_CLIENT_SECRET = "RlL3T1Cw5mugUs05ZYlc63hEwgEVgRTpXx_sASex5rZrh2kTpEGWs8VkiGfIZt5u";

        // The `offline_access` scope enables us to request Refresh Tokens, so we don't have to ask the
        // user to authorise us again every time the tokens expire. Some providers might have an
        // `offline` scope instead. If you get an `invalid_scope` error when trying to authorise the
        // app, try changing it to `offline`.
        public static final String[] TEST_SCOPES = {"https://testresource-server.openstack.org/summits/read"};
        public static final String[] PRODUCTION_SCOPES = {"https://openstackid-resources.openstack.org/summits/read"};
    }

    /**
     * Simple utility class for storing OpenID Connect configuration. This should not be used in
     * production. If you want to hide your keys, you should obfuscate them using ProGuard (with added
     * manual obfuscation), DexGuard or something else.
     *
     * See this Stack Overflow post for some suggestions:
     * https://stackoverflow.com/a/14570989
     */
    public static class ConfigOIDC {
        public static final String TEST_CLIENT_ID = "9HKaikkxU~6Qvor_GvhQm6ADYi7QmwUL.openstack.client";
        public static final String TEST_CLIENT_SECRET = "oWqW0hf49Im5Iyk-z59DsOekc9FZbcfVc4o9sGGX6gw3Ef6NRntlhqWEwzDGYPfT";
        public static final String PRODUCTION_CLIENT_ID = "Gg2_KiXCd3OO1f8Gas_cy7RSR1g8EApY.openstack.client";
        public static final String PRODUCTION_CLIENT_SECRET = "JgcmXHFR7s88KWd1J5hTL9Z70mSs2IAo8NnHTWCOsz.N16hMxXUUCXur6v3gg9Ma";

        // This URL doesn't really have a use with native apps and basically just signifies the end
        // of the authorisation process. It doesn't have to be a real URL, but it does have to be the
        // same URL that is registered with your provider.
        public static final String REDIRECT_URL = "org.openstack.android.summit://oauthCallback";

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

        // The `offline_access` scope enables us to request Refresh Tokens, so we don't have to ask the
        // user to authorise us again every time the tokens expire. Some providers might have an
        // `offline` scope instead. If you get an `invalid_scope` error when trying to authorise the
        // app, try changing it to `offline`.
        public static final String[] TEST_SCOPES = {"openid", "offline_access", "https://testresource-server.openstack.org/summits/read", "https://testresource-server.openstack.org/summits/write"};
        public static final String[] PRODUCTION_SCOPES = {"openid", "offline_access", "https://openstackid-resources.openstack.org/summits/read", "https://openstackid-resources.openstack.org/summits/write"};
    }
}
