package org.openstack.android.summit.modules.general_schedule.user_interface;

import org.openstack.android.summit.common.user_interface.IScheduleView;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public interface IGeneralScheduleView extends IScheduleView {
    void toggleEventList(boolean show);

    void toggleNoConnectivityMessage(boolean show);
}
