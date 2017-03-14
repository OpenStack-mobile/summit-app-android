package org.openstack.android.summit.common.user_interface;

import android.content.Intent;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;

/**
 * Created by smarcet on 3/12/17.
 */

public class ShareIntentBuilder {

    public static Intent build(String eventSocialSummary, String eventUrl){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        if(eventSocialSummary == null || eventSocialSummary.trim().isEmpty())
            eventSocialSummary =  OpenStackSummitApplication.context.getResources().getString(R.string.share_event_text);
        shareIntent.putExtra(Intent.EXTRA_TEXT, eventSocialSummary.trim() + " "+eventUrl.trim());
        return shareIntent;
    }
}
