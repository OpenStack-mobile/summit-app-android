package org.openstack.android.summit.common;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 12/29/2015.
 */
public class ScheduleWireframe implements IScheduleWireframe {
    IEventDetailWireframe eventDetailWireframe;

    @Inject
    public ScheduleWireframe(IEventDetailWireframe eventDetailWireframe) {
        this.eventDetailWireframe = eventDetailWireframe;
    }

    @Override
    public void showEventDetail(FragmentActivity context) {
        eventDetailWireframe.presentEventDetailView(context);
    }
}
