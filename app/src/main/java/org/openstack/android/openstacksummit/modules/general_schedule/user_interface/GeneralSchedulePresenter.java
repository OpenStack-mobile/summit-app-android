package org.openstack.android.openstacksummit.modules.general_schedule.user_interface;

import org.openstack.android.openstacksummit.common.user_interface.IPresenter;
import org.openstack.android.openstacksummit.modules.general_schedule.GeneralScheduleWireframe;
import org.openstack.android.openstacksummit.modules.general_schedule.business_logic.GeneralScheduleInteractor;

import javax.inject.Inject;

/**
 * Created by claudio on 10/30/2015.
 */
public class GeneralSchedulePresenter implements IPresenter {

    @Inject
    public GeneralSchedulePresenter(GeneralScheduleInteractor generalScheduleInteractor, GeneralScheduleWireframe generalScheduleWireframe) {
        this.interactor = interactor;
        this.wireframe = wireframe;
    }

    GeneralScheduleFragment view;
    GeneralScheduleInteractor interactor;
    GeneralScheduleWireframe wireframe;

    public void setView(GeneralScheduleFragment view) {
        this.view = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
}
