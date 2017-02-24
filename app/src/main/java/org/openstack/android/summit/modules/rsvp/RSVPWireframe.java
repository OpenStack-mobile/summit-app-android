package org.openstack.android.summit.modules.rsvp;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.openstack.android.summit.common.BaseWireframe;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.common.utils.IAppLinkRouter;

/**
 * Created by smarcet on 2/23/17.
 */

public class RSVPWireframe extends BaseWireframe implements IRSVPWireframe {
    private IAppLinkRouter appLinkRouter;

    public RSVPWireframe(IAppLinkRouter appLinkRouter, INavigationParametersStore navigationParametersStore) {
        super(navigationParametersStore);
        this.appLinkRouter = appLinkRouter;
    }

    @Override
    public void presentEventRsvpView(String rsvpLink, IBaseView context) {
        //before check if we are trying to see a custom rsvp
        if(rsvpLink == null || rsvpLink.isEmpty()) return;
        Uri uri  = Uri.parse(rsvpLink);
        Intent i = null;
        if(this.appLinkRouter.isCustomRSVPLink(uri)){
            Log.i(Constants.LOG_TAG, String.format("opening custom RSVP template %s...", uri.toString()));
            // match! rsvp browser
            i = new Intent(context.getApplicationContext(), RSVPViewerActivity.class);
            i.setData(uri);
        } else {
            Log.i(Constants.LOG_TAG, String.format("opening Third party RSVP link %s ...", uri.toString()));
            i = new Intent(Intent.ACTION_VIEW, uri);
        }
        context.startActivity(i);
    }
}
