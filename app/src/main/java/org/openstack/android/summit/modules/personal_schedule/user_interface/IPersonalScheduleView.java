package org.openstack.android.summit.modules.personal_schedule.user_interface;

import org.openstack.android.summit.common.user_interface.IScheduleView;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public interface IPersonalScheduleView extends IScheduleView {
    
    void removeItem(int position);

    void enableListView(boolean enable);
}
