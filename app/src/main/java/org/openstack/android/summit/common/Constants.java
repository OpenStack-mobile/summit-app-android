package org.openstack.android.summit.common;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public class Constants {
    static public final String LOG_TAG = "OpenStackSummit";
    static public final String GENERIC_ERROR_MSG = "There was an error performing this operation.";
    static public final String TEST_TOKEN_SERVER_URL = "https://testopenstackid.openstack.org/oauth2/token";
    static public final String TEST_AUTHORIZATION_SERVER_URL = "https://testopenstackid.openstack.org/oauth2/auth";
    static public final String TEST_RESOURCE_SERVER_BASE_URL = "https://testresource-server.openstack.org";
    static public final String TEST_USER_INFO_SERVER_URL = "https://testopenstackid.openstack.org/api/v1/users/me";
    static public final String PRODUCTION_TOKEN_SERVER_URL = "https://openstackid.org/oauth2/token";
    static public final String PRODUCTION_AUTHORIZATION_SERVER_URL = "https://openstackid.org/oauth2/auth";
    static public final String PRODUCTION_RESOURCE_SERVER_BASE_URL = "https://openstackid-resources.openstack.org";
    static public final String PRODUCTION_USER_INFO_SERVER_URL = "https://openstackid.org/api/v1/users/me";

    static public final String CURRENT_MEMBER_ID = "CurrentMemberId";
    static public final String CURRENT_MEMBER_NAME = "CurrentMemberName";
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
        //WARNING: these are enconded!!!
        public static final String TEST_CLIENT_ID = "Sll2ei5BS1R6bHBMZlNsNEcuaTQyQWJwLWU1ZFZHVm4ub3BlbnN0YWNrLmNsaWVudA==";
        public static final String TEST_CLIENT_SECRET = "bFF4SE1yZDU3NlRLLmlMWEZYVks3bXB4WTFoT2llfmdENzFUXzJIMW83a1IwR2xzdFNlb21NVX5yZk1meV9WeQ==";
        public static final String PRODUCTION_CLIENT_ID = "aDcwVXBYYlNsT0F4LWkucFl4UERNN3FFMDNGVzJuV0Yub3BlbnN0YWNrLmNsaWVudA==";
        public static final String PRODUCTION_CLIENT_SECRET = "UmxMM1QxQ3c1bXVnVXMwNVpZbGM2M2hFd2dFVmdSVHBYeF9zQVNleDVyWnJoMmtUcEVHV3M4VmtpR2ZJWnQ1dQ==";

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
        //WARNING: these are enconded!!!
        public static final String TEST_CLIENT_ID = "OUhLYWlra3hVfjZRdm9yX0d2aFFtNkFEWWk3UW13VUwub3BlbnN0YWNrLmNsaWVudA==";
        public static final String TEST_CLIENT_SECRET = "b1dxVzBoZjQ5SW01SXlrLXo1OURzT2VrYzlGWmJjZlZjNG85c0dHWDZndzNFZjZOUm50bGhxV0V3ekRHWVBmVA==";
        public static final String PRODUCTION_CLIENT_ID = "R2cyX0tpWENkM09PMWY4R2FzX2N5N1JTUjFnOEVBcFkub3BlbnN0YWNrLmNsaWVudA==";
        public static final String PRODUCTION_CLIENT_SECRET = "SmdjbVhIRlI3czg4S1dkMUo1aFRMOVo3MG1TczJJQW84Tm5IVFdDT3N6Lk4xNmhNeFhVVUNYdXI2djNnZzlNYQ==";

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
        public static final String[] TEST_SCOPES = {
                "openid",
                "offline_access",
                "profile",
                "https://testresource-server.openstack.org/summits/read",
                "https://testresource-server.openstack.org/summits/write",
                "https://testresource-server.openstack.org/summits/read-external-orders",
                "https://testresource-server.openstack.org/summits/confirm-external-orders"
        };
        public static final String[] PRODUCTION_SCOPES = {
                "openid",
                "offline_access",
                "profile",
                "https://openstackid-resources.openstack.org/summits/read",
                "https://openstackid-resources.openstack.org/summits/write",
                "https://openstackid-resources.openstack.org/summits/read-external-orders",
                "https://openstackid-resources.openstack.org/summits/confirm-external-orders"
        };
    }
}
