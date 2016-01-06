package org.openstack.android.summit.common.user_interface;

import org.openstack.android.summit.common.IScheduleWireframe;
import org.openstack.android.summit.common.business_logic.IScheduleInteractor;

/**
 * Created by Claudio Redi on 12/28/2015.
 */
public interface ISchedulePresenter<V extends ScheduleFragment, I extends IScheduleInteractor, W extends IScheduleWireframe> extends IPresenter<V> {
    void buildItem(IScheduleItemView scheduleItemView, int position);

    void reloadSchedule();

    void toggleScheduleStatus(IScheduleItemView scheduleItemView, int position);
}

