package org.openstack.android.summit.modules.events.user_interface;

import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.business_logic.IInteractorOperationListener;
import org.openstack.android.summit.common.business_logic.IScheduleInteractor;
import org.openstack.android.summit.common.user_interface.IPresenter;
import org.openstack.android.summit.modules.events.IEventsWireframe;

import javax.inject.Inject;

/**
 * Created by claudio on 10/30/2015.
 */
public class EventsPresenter implements IPresenter, IInteractorOperationListener<SummitDTO> {

    @Inject
    public EventsPresenter(IScheduleInteractor generalScheduleInteractor, IEventsWireframe generalScheduleWireframe) {
        this.interactor = generalScheduleInteractor;
        this.wireframe = generalScheduleWireframe;
        this.interactor.setDelegate(this);
    }

    private EventsFragment view;
    private IScheduleInteractor interactor;
    private IEventsWireframe wireframe;

    public void setView(EventsFragment view) {
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

    @Override
    public void onSuceedWithData(SummitDTO data) {

    }

    @Override
    public void onSucceed() {

    }

    @Override
    public void onError(String message) {

    }
}
