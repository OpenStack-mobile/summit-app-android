package org.openstack.android.summit.common;

/**
 * Created by Claudio Redi on 11/17/2015.
 */

public class Constants {
    static public final String LOG_TAG                                = "OpenStackSummit";
    static public final String GENERIC_ERROR_MSG                      = "There was an error performing this operation.";
    static public final String CURRENT_MEMBER_ID                      = "CurrentMemberId";
    static public final String LOGGED_IN_EVENT                        = "logged-in-event";
    static public final String START_LOG_IN_EVENT                     = "start-logged-in-event";
    static public final String ON_LOGGING_PROCESS                     = "on-logging-process";
    static public final String LOG_IN_ERROR_EVENT                     = "log-in-error-event";
    static public final String LOG_IN_CANCELLED_EVENT                 = "log-in-cancelled-event";
    static public final String LOG_IN_ERROR_MESSAGE                   = "log-in-error-message";
    static public final String LOGGED_OUT_EVENT                       = "logged-out-event";
    static public final String PUSH_NOTIFICATION_RECEIVED             = "push-notification-received-event";
    static public final String PUSH_NOTIFICATION_DELETED              = "push-notification-deleted-event";
    static public final String PUSH_NOTIFICATION_OPENED               = "push-notification-opened-event";
    static public final String WIPE_DATE_EVENT                        = "wipe-data-event";
    static public final String NAVIGATION_PARAMETER_SEARCH_TERM       = "SearchTerm";
    static public final String NAVIGATION_PARAMETER_EVENT_ID          = "EventId";
    static public final String NAVIGATION_PARAMETER_EVENT_RATE        = "EventRate";
    public static final String NAVIGATION_PARAMETER_LEVEL             = "Level";
    public static final String NAVIGATION_PARAMETER_TRACK             = "Track";
    public static final String NAVIGATION_PARAMETER_IS_MY_PROFILE     = "IsMyProfile";
    public static final String NAVIGATION_PARAMETER_SPEAKER           = "SpeakerId";
    public static final String NAVIGATION_PARAMETER_NOTIFICATION_ID   = "NotificationId";
    public static final String NAVIGATION_PARAMETER_VENUE             = "VenueId";
    public static final String NAVIGATION_PARAMETER_ROOM              = "roomId";
    public static final String BASIC_AUTH_USER                        = "org.openstack.android.summit.common.security.BASIC_AUTH_USER";
    public static final String BASIC_AUTH_PASS                        = "org.openstack.android.summit.common.security.BASIC_AUTH_PASS";
    public static final String FLAVOR_DEV                             = "development";
    public static final String FLAVOR_BETA                            = "beta";
    public static final String SETTING_BLOCK_NOTIFICATIONS_KEY        = "BlockNotificationsSetting";
    public static final String DATA_UPDATE_ADDED_ENTITY_EVENT         = "data-update-added-entity";
    public static final String DATA_UPDATE_UPDATED_ENTITY_EVENT       = "data-update-updated-entity";
    public static final String DATA_UPDATE_DELETED_ENTITY_EVENT       = "data-update-deleted-entity";
    public static final String DATA_UPDATE_ENTITY_ID                  = "data-update-entity-id";
    public static final String DATA_UPDATE_ENTITY_CLASS               = "data-update-entity-class";
    public static final String KEY_DATA_UPDATE_SET_FROM_DATE          = "KEY_DATA_UPDATE_SET_FROM_DATE";
    public static final String KEY_DATA_UPDATE_LAST_WIPE_EVENT_ID     = "KEY_DATA_UPDATE_LAST_WIPE_EVENT_ID";
    public static final String KEY_DATA_UPDATE_LAST_EVENT_ID          = "KEY_DATA_UPDATE_LAST_EVENT_ID";
    public static final String EXTRA_ENABLE_DATA_UPDATES_AFTER_LOGOUT = "EXTRA_ENABLE_DATA_UPDATES_AFTER_LOGOUT";
    public static final String CURRENT_SUMMIT_ID                      = "CURRENT_SESSION_ID";
    static public final String ON_DATA_LOADING_PROCESS                = "on-data-loading-process";
    static public final String LOADED_SUMMITS_LIST                    = "loaded-summits-list";
}