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
        if(isInternalAppLink(url)) return true;
        if(isMainScheduleFilteredByTrack(url)) return true;
        if(isMainScheduleFilteredByLevel(url)) return true;
        return false;
    }

    private boolean isInternalAppLink(Uri url){
        if(!url.getScheme().toLowerCase().contains(DeepLinkHost)) return false;
        if(url.getHost().toLowerCase().contains(DeepLinkInfo.EventsPath)) return true;
        if(url.getHost().toLowerCase().contains(DeepLinkInfo.SpeakersPath)) return true;
        if(url.getHost().toLowerCase().contains(DeepLinkInfo.LocationsPath)) return true;
        if(url.getHost().toLowerCase().contains(DeepLinkInfo.SchedulePath)) return true;
        if(url.getHost().toLowerCase().contains(DeepLinkInfo.NotificationsPath)) return true;
        if(url.getHost().toLowerCase().contains(DeepLinkInfo.LevelPath)) return true;
        if(url.getHost().toLowerCase().contains(DeepLinkInfo.TrackPath)) return true;
        if(url.getHost().toLowerCase().contains(DeepLinkInfo.SearchPath)) return true;
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
    public boolean isMainScheduleFilteredByTrack(Uri url) {
        if(!url.getLastPathSegment().equals("summit-schedule")) return false;
        String fragment = url.getFragment();
        return fragment != null && fragment.contains("track=");
    }

    @Override
    public boolean isMainScheduleFilteredByLevel(Uri url) {
        if(!url.getLastPathSegment().equals("summit-schedule")) return false;
        String fragment = url.getFragment();
        return fragment != null && fragment.contains("level=");
    }

    @Override
    public boolean isRawMainSchedule(Uri url) {
        return url.getLastPathSegment().equals("summit-schedule");
    }

    @Override
    public DeepLinkInfo buildDeepLinkInfo(Uri url) {
        if(isInternalAppLink(url)){
            String action = url.getHost();
            String param  = url.getPath().replace("/","");
            return new DeepLinkInfo(action, param);
        }
        if(isMainScheduleFilteredByTrack(url)){
            String action   = DeepLinkInfo.TrackPath;
            String urlNew   = url.toString().replace("#", "?");
            String param    = Uri.parse(urlNew).getQueryParameter("track");
            return new DeepLinkInfo(action, param);
        }
        if(isMainScheduleFilteredByLevel(url)){
            String action   = DeepLinkInfo.LevelPath;
            String urlNew   = url.toString().replace("#", "?");
            String param    = Uri.parse(urlNew).getQueryParameter("level");
            return new DeepLinkInfo(action, param);
        }
        return null;
    }

    @Override
    public Uri buildUriFor(String action, String param){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(DeepLinkHost).authority(action).path(param);
        return builder.build();
    }
}
