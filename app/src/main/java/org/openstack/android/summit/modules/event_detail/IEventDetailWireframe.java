package org.openstack.android.summit.modules.event_detail;

import org.openstack.android.summit.common.IBaseWireframe;
import org.openstack.android.summit.common.IScheduleWireframe;
import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
public interface IEventDetailWireframe extends IScheduleWireframe {

    void showSpeakerProfile(int speakerId, IBaseView view);

    void showFeedbackEditView(int eventId, int rate, IBaseView view);

    void showEventVenueDetailView(int venueId, IBaseView view);

    void showEventLocationDetailView(int locationId, IBaseView view);

    void presentLevelScheduleView(String level,  IBaseView view);
}
