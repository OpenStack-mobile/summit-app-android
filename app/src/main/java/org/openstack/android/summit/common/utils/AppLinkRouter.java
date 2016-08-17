package org.openstack.android.summit.common.utils;

import android.net.Uri;

/**
 * Created by sebastian on 8/2/2016.
 */
final public class AppLinkRouter implements IAppLinkRouter {

    public static final String DeepLinkHost = "org.openstack.android.summit";
    @Override
    public boolean isDeepLink(Uri url) {
        if(!url.getScheme().toLowerCase().contains(DeepLinkHost)) return false;
        if(url.getHost().toLowerCase().contains(DeepLinkInfo.EventsPath)) return true;
        if(url.getHost().toLowerCase().contains(DeepLinkInfo.SpeakersPath)) return true;
        if(url.getHost().toLowerCase().contains(DeepLinkInfo.LocationsPath)) return true;
        if(url.getHost().toLowerCase().contains(DeepLinkInfo.SchedulePath)) return true;
        return false;
    }

    @Override
    public DeepLinkInfo buildDeepLinkInfo(Uri url) {
        String action = url.getHost();
        String param  = url.getPath().replace("/","");
        return new DeepLinkInfo(action, param);
    }
}
