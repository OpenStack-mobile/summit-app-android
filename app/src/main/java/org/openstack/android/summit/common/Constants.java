package org.openstack.android.summit.common;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public class Constants {
    static public final String LOG_TAG = "OpenStackSummit";
    static public final String TEST_TOKEN_SERVER_URL = "https://testopenstackid.openstack.org/oauth2/token";
    static public final String TEST_AUTHORIZATION_SERVER_URL = "https://testopenstackid.openstack.org/oauth2/auth";
    static public final String TEST_RESOURCE_SERVER_BASE_URL = "https://testresource-server.openstack.org";
    static public final String PRODUCTION_TOKEN_SERVER_URL = "https://openstackid.org/oauth2/token";
    static public final String PRODUCTION_AUTHORIZATION_SERVER_URL = "https://openstackid.org/oauth2/auth";
    static public final String PRODUCTION_RESOURCE_SERVER_BASE_URL = "https://openstackid-resources.openstack.org";

    // The `offline_access` scope enables us to request Refresh Tokens, so we don't have to ask the
    // user to authorise us again every time the tokens expire. Some providers might have an
    // `offline` scope instead. If you get an `invalid_scope` error when trying to authorise the
    // app, try changing it to `offline`.
    public static final String[] PRODUCTION_SCOPES = {"openid", "offline_access", "https://testresource-server.openstack.org/summits/read", "https://testresource-server.openstack.org/summits/write"};
    public static final String[] TEST_SCOPES = {"openid", "offline_access", "https://openstackid-resources.openstack.org/summits/read", "https://openstackid-resources.openstack.org/summits/write"};


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
}
