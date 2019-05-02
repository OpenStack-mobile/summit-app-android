package org.openstack.android.summit.modules.level_schedule;

import androidx.fragment.app.FragmentActivity;

import org.openstack.android.summit.common.IScheduleWireframe;
import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public interface ILevelScheduleWireframe extends IScheduleWireframe {

    void presentLevelScheduleView(String level, IBaseView context);

    void showFilterView(IBaseView view);
}
