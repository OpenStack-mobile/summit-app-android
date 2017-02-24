package org.openstack.android.summit.modules.general_schedule;

import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.ScheduleWireframe;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.rsvp.IRSVPWireframe;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 12/21/2015.
 */
public class GeneralScheduleWireframe extends ScheduleWireframe implements IGeneralScheduleWireframe {

    @Inject
    public GeneralScheduleWireframe
    (   IRSVPWireframe rsvpWireframe,
        IEventDetailWireframe eventDetailWireframe,
        INavigationParametersStore navigationParametersStore
    )
    {
        super(eventDetailWireframe, rsvpWireframe, navigationParametersStore);
    }
}
