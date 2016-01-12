package org.openstack.android.summit.modules.general_schedule.user_interface;

import org.openstack.android.summit.common.user_interface.SchedulePresenter;
import org.openstack.android.summit.modules.general_schedule.GeneralScheduleWireframe;
import org.openstack.android.summit.modules.general_schedule.IGeneralScheduleWireframe;
import org.openstack.android.summit.modules.general_schedule.business_logic.GeneralScheduleInteractor;
import org.openstack.android.summit.modules.general_schedule.business_logic.IGeneralScheduleInteractor;

/**
 * Created by Claudio Redi on 12/21/2015.
 */
public class GeneralSchedulePresenter extends SchedulePresenter<GeneralScheduleFragment, IGeneralScheduleInteractor, IGeneralScheduleWireframe> implements IGeneralSchedulePresenter {
    public GeneralSchedulePresenter(IGeneralScheduleInteractor interactor, IGeneralScheduleWireframe wireframe) {
        super(interactor, wireframe);
    }
}