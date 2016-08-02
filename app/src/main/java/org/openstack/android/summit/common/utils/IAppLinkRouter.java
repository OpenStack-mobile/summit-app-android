package org.openstack.android.summit.common.utils;

import android.net.Uri;

/**
 * Created by sebastian on 8/2/2016.
 */
public interface IAppLinkRouter {

    boolean isDeepLink(Uri url);

    DeepLinkInfo buildDeepLinkInfo(Uri url);
}
