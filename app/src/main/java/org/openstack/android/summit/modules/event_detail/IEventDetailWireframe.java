package org.openstack.android.summit.modules.event_detail;

import android.content.Intent;
import org.openstack.android.summit.common.IBaseWireframe;
import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public interface IEventDetailWireframe extends IBaseWireframe {

    void presentEventDetailView(int eventId, IBaseView context);

    void showSpeakerProfile(int speakerId, IBaseView view);

    void showFeedbackEditView(int eventId, IBaseView view);

    void showEventVenueDetailView(int venueId, IBaseView view);

    void presentEventRsvpView(String rsvpLink, IBaseView view);

    void showEventLocationDetailView(int locationId, IBaseView view);
}
