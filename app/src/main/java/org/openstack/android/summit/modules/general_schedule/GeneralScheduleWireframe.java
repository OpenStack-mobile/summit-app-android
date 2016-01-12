package org.openstack.android.summit.modules.general_schedule;

import android.app.Activity;

import org.openstack.android.summit.common.IScheduleWireframe;
import org.openstack.android.summit.common.ScheduleWireframe;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 12/21/2015.
 */
public class GeneralScheduleWireframe extends ScheduleWireframe implements IGeneralScheduleWireframe {

    @Inject
    public GeneralScheduleWireframe(IEventDetailWireframe eventDetailWireframe) {
        super(eventDetailWireframe);
    }
}
