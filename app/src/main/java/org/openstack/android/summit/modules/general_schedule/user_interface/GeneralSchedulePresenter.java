package org.openstack.android.summit.modules.general_schedule.user_interface;

import org.openstack.android.summit.common.user_interface.SchedulePresenter;
import org.openstack.android.summit.modules.general_schedule.GeneralScheduleWireframe;
import org.openstack.android.summit.modules.general_schedule.business_logic.GeneralScheduleInteractor;

/**
 * Created by Claudio Redi on 12/21/2015.
 */
public class GeneralSchedulePresenter extends SchedulePresenter<GeneralScheduleFragment, GeneralScheduleInteractor, GeneralScheduleWireframe> {
    public GeneralSchedulePresenter(GeneralScheduleInteractor interactor, GeneralScheduleWireframe wireframe) {
        super(interactor, wireframe);
    }
}