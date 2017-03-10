package org.openstack.android.summit.modules.general_schedule;

import org.openstack.android.summit.common.IScheduleWireframe;
import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by Claudio Redi on 12/21/2015.
 */
public interface IGeneralScheduleWireframe extends IScheduleWireframe {

    void showFilterView(IBaseView view);
}