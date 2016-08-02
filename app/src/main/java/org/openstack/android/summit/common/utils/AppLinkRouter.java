package org.openstack.android.summit.common.utils;

import android.net.Uri;

/**
 * Created by sebastian on 8/2/2016.
 */
final public class AppLinkRouter implements IAppLinkRouter {

    @Override
    public boolean isDeepLink(Uri url) {
        if(!url.getScheme().startsWith("org.openstack.android.summi")) return false;
        if(url.getHost().startsWith("events")) return true;
        if(url.getHost().startsWith("speakers")) return true;
        if(url.getHost().startsWith("locations")) return true;
        if(url.getHost().startsWith("schedule")) return true;
        return false;
    }

    @Override
    public DeepLinkInfo buildDeepLinkInfo(Uri url) {
        String action = url.getHost();
        String param  = url.getPath().replace("/","");
        return new DeepLinkInfo(param, action);
    }
}
