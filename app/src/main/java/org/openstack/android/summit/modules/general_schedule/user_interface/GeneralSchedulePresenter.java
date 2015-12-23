package org.openstack.android.summit.modules.general_schedule.user_interface;

import android.os.Bundle;

import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.business_logic.InteractorAsyncOperationListener;
import org.openstack.android.summit.common.user_interface.IScheduleItem;
import org.openstack.android.summit.modules.general_schedule.IGeneralScheduleWireframe;
import org.openstack.android.summit.modules.general_schedule.business_logic.IGeneralScheduleInteractor;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 12/21/2015.
 */
public class GeneralSchedulePresenter implements IGeneralSchedulePresenter {
    GeneralScheduleFragment view;
    IGeneralScheduleInteractor interactor;
    IGeneralScheduleWireframe wireframe;
    InteractorAsyncOperationListener<ScheduleItemDTO> scheduleItemDTOIInteractorOperationListener;
    List<ScheduleItemDTO> events;

    @Inject
    public GeneralSchedulePresenter(IGeneralScheduleInteractor interactor, IGeneralScheduleWireframe wireframe) {
        this.interactor = interactor;
        this.wireframe = wireframe;
        scheduleItemDTOIInteractorOperationListener = new InteractorAsyncOperationListener<ScheduleItemDTO>() {
            @Override
            public void onSuceedWithData(ScheduleItemDTO data) {
                super.onSuceedWithData(data);
            }

            @Override
            public void onError(String message) {
                super.onError(message);
            }
        };
    }

    public void setView(GeneralScheduleFragment view) {
        this.view = view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        InteractorAsyncOperationListener<SummitDTO> summitDTOIInteractorOperationListener = new InteractorAsyncOperationListener<SummitDTO>() {
            @Override
            public void onSuceedWithData(SummitDTO data) {
                events = interactor.getScheduleEvents(new Date(1440192629L*1000), new Date(1450792629L*1000), null, null, null, null, null);
                view.setEvents(events);
            }

            @Override
            public void onError(String message) {
                super.onError(message);
            }
        };

        interactor.getActiveSummit(summitDTOIInteractorOperationListener);
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

    public void buildItem(IScheduleItem item, int position) {
        ScheduleItemDTO scheduleItemDTO = events.get(position);
        item.setName(scheduleItemDTO.getName());
        item.setTime(scheduleItemDTO.getTime());
    }
}
