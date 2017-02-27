package org.openstack.android.summit.modules.level_schedule;

import android.support.v4.app.FragmentActivity;

import org.openstack.android.summit.common.IScheduleWireframe;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.level_schedule.user_interface.ILevelScheduleView;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public interface ILevelScheduleWireframe extends IScheduleWireframe {

    void presentLevelScheduleView(String level, IBaseView context);

    void showFilterView(IBaseView view);
}
