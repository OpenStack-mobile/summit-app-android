package org.openstack.android.summit.common.utils;

/**
 * Created by sebastian on 8/2/2016.
 */
public class DeepLinkInfo {

    public static final String ActionViewEvent    = "VIEW_EVENT";
    public static final String ActionViewSpeaker  = "VIEW_SPEAKER";
    public static final String ActionViewLocation = "VIEW_LOCATION";
    public static final String ActionViewSchedule = "VIEW_SCHEDULE";

    private String param;
    private String action;

    public DeepLinkInfo(String param, String action) {
        this.param  = param;
        if(action.startsWith("events"))    this.action = ActionViewEvent;
        if(action.startsWith("speakers"))  this.action = ActionViewSpeaker;
        if(action.startsWith("locations")) this.action = ActionViewLocation;
        if(action.startsWith("schedule"))  this.action = ActionViewSchedule;
    }

    public String getParam() {
        return param;
    }

    public boolean hasParam(){
        return !param.isEmpty();
    }

    public int getParamAsInt() {
        return Integer.parseInt(param);
    }

    public String getAction() {
        return action;
    }
}
