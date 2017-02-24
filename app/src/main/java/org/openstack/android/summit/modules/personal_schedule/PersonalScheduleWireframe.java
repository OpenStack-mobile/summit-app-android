package org.openstack.android.summit.modules.personal_schedule;

import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.ScheduleWireframe;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.rsvp.IRSVPWireframe;

/**
 * Created by Claudio Redi on 1/28/2016.
 */
public class PersonalScheduleWireframe extends ScheduleWireframe implements IPersonalScheduleWireframe {
    public PersonalScheduleWireframe
(
        IEventDetailWireframe eventDetailWireframe,
        IRSVPWireframe rsvpWireframe,
        INavigationParametersStore navigationParametersStore
    )
    {
        super(eventDetailWireframe, rsvpWireframe, navigationParametersStore);
    }
}
