package org.openstack.android.summit.modules.favorites_schedule;

import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.ScheduleWireframe;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.rsvp.IRSVPWireframe;

/**
 * Created by smarcet on 3/14/17.
 */

public class FavoritesScheduleWireframe extends ScheduleWireframe implements IFavoritesScheduleWireframe {
    public FavoritesScheduleWireframe
    (
        IEventDetailWireframe eventDetailWireframe,
        IRSVPWireframe rsvpWireframe,
        INavigationParametersStore navigationParametersStore
    )
    {
        super(eventDetailWireframe, rsvpWireframe, navigationParametersStore);
    }
}