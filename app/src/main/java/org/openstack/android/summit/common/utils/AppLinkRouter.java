package org.openstack.android.summit.common.utils;

import android.net.Uri;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        if(url.getHost().toLowerCase().contains(DeepLinkInfo.NotificationsPath)) return true;
        if(url.getHost().toLowerCase().contains(DeepLinkInfo.LevelPath)) return true;
        if(url.getHost().toLowerCase().contains(DeepLinkInfo.TrackPath)) return true;
        return false;
    }

    @Override
    public boolean isCustomRSVPLink(Uri url){
        // get the app link metadata
        //before check if we are trying to see a custom rsvp
        Pattern r = Pattern.compile(".*/summit/.*/.*/events/\\d+/.*/rsvp");
        Matcher m = r.matcher(url.toString());
        return m.find();
    }

    @Override
    public DeepLinkInfo buildDeepLinkInfo(Uri url) {
        String action = url.getHost();
        String param  = url.getPath().replace("/","");
        return new DeepLinkInfo(action, param);
    }

    @Override
    public Uri buildUriFor(String action, String param){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(DeepLinkHost).authority(action).path(param);
        return builder.build();
    }
}
