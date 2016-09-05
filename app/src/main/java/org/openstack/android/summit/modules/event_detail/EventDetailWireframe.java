package org.openstack.android.summit.modules.event_detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.BaseWireframe;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.common.utils.IAppLinkRouter;
import org.openstack.android.summit.modules.event_detail.user_interface.EventDetailFragment;
import org.openstack.android.summit.modules.feedback_edit.IFeedbackEditWireframe;
import org.openstack.android.summit.modules.member_profile.IMemberProfileWireframe;
import org.openstack.android.summit.modules.rsvp.RSVPViewerActivity;
import org.openstack.android.summit.modules.venue_detail.IVenueDetailWireframe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

/**
 * Created by claudio on 11/2/2015.
 */
public class EventDetailWireframe extends BaseWireframe implements IEventDetailWireframe {

    private IMemberProfileWireframe memberProfileWireframe;
    private IFeedbackEditWireframe feedbackEditWireframe;
    private IVenueDetailWireframe venueDetailWireframe;
    private IAppLinkRouter appLinkRouter;

    @Inject
    public EventDetailWireframe(IMemberProfileWireframe memberProfileWireframe, IFeedbackEditWireframe feedbackEditWireframe, INavigationParametersStore navigationParametersStore, IVenueDetailWireframe venueDetailWireframe, IAppLinkRouter appLinkRouter) {
        super(navigationParametersStore);

        this.memberProfileWireframe = memberProfileWireframe;
        this.feedbackEditWireframe  = feedbackEditWireframe;
        this.venueDetailWireframe   = venueDetailWireframe;
        this.appLinkRouter          = appLinkRouter;
    }

    @Override
    public void presentEventDetailView(int eventId, IBaseView context) {
        try {
            navigationParametersStore.put(Constants.NAVIGATION_PARAMETER_EVENT_ID, eventId);

            EventDetailFragment eventDetailFragment = new EventDetailFragment();
            FragmentManager fragmentManager = context.getSupportFragmentManager();

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout_content, eventDetailFragment)
                    .addToBackStack(null)
                    .commit();
        } catch (Exception e) {
            // Swallowing exception "Can not perform this action after onSaveInstanceState" until we figure out what's wrong
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
        }
    }

    @Override
    public void presentEventRsvpView(String rsvpLink, IBaseView context) {
        //before check if we are trying to see a custom rsvp
        Uri uri  = Uri.parse(rsvpLink);
        Intent i = null;
        if(this.appLinkRouter.isCustomRSVPLink(uri)){
            Log.d(Constants.LOG_TAG, "opening custom RSVP template ...");
            // match! rsvp browser
            i = new Intent(context.getApplicationContext(), RSVPViewerActivity.class);
            i.setData(uri);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        } else {
            i = new Intent(Intent.ACTION_VIEW, uri);
        }
        context.startActivity(i);
    }

    @Override
    public void showSpeakerProfile(int speakerId, IBaseView context) {
        memberProfileWireframe.presentOtherSpeakerProfileView(speakerId, context);
    }

    @Override
    public void showFeedbackEditView(int eventId, IBaseView view) {
        feedbackEditWireframe.presentFeedbackEditView(eventId, view);
    }

    @Override
    public void showEventVenueDetailView(int venueId, IBaseView view) {
        NamedDTO venue = new NamedDTO();
        venue.setId(venueId);
        venueDetailWireframe.presentVenueDetailView(venue, view);
    }

    @Override
    public void showEventLocationDetailView(int locationId, IBaseView view) {
        NamedDTO location = new NamedDTO();
        location.setId(locationId);
        venueDetailWireframe.presentLocationDetailView(location, view);
    }
}
