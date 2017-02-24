package org.openstack.android.summit.common;

import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.rsvp.IRSVPWireframe;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 12/29/2015.
 */
public class ScheduleWireframe extends BaseWireframe implements IScheduleWireframe {

    IEventDetailWireframe eventDetailWireframe;
    IRSVPWireframe rsvpWireframe;

    @Inject
    public ScheduleWireframe
    (
                    IEventDetailWireframe eventDetailWireframe,
                    IRSVPWireframe rsvpWireframe,
                    INavigationParametersStore navigationParametersStore
    )
    {
        super(navigationParametersStore);
        this.rsvpWireframe        = rsvpWireframe;
        this.eventDetailWireframe = eventDetailWireframe;
    }

    @Override
    public void showEventDetail(int eventId, IBaseView context) {
        eventDetailWireframe.presentEventDetailView(eventId, context);
    }

    @Override
    public void presentEventRsvpView(String rsvpLink, IBaseView context) {
        rsvpWireframe.presentEventRsvpView(rsvpLink, context);
    }
}
