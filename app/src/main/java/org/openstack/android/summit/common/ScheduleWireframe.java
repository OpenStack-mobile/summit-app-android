package org.openstack.android.summit.common;

import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 12/29/2015.
 */
public class ScheduleWireframe extends BaseWireframe implements IScheduleWireframe {
    IEventDetailWireframe eventDetailWireframe;

    @Inject
    public ScheduleWireframe(IEventDetailWireframe eventDetailWireframe, INavigationParametersStore navigationParametersStore) {
        super(navigationParametersStore);
        this.eventDetailWireframe = eventDetailWireframe;
    }

    @Override
    public void showEventDetail(int eventId, IBaseView context) {
        eventDetailWireframe.presentEventDetailView(eventId, context);
    }
}
