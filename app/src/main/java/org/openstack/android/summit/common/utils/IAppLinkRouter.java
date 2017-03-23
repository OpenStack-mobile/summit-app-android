package org.openstack.android.summit.common.utils;

import android.net.Uri;

/**
 * Created by sebastian on 8/2/2016.
 */
public interface IAppLinkRouter {

    boolean isDeepLink(Uri url);

    DeepLinkInfo buildDeepLinkInfo(Uri url);

    Uri buildUriFor(String action, String param);

    boolean isCustomRSVPLink(Uri url);

    boolean isMainScheduleFilteredByTrack(Uri url);

    boolean isMainScheduleFilteredByLevel(Uri url);

    boolean isRawMainSchedule(Uri url);
}
