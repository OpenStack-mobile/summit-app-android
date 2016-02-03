package org.openstack.android.summit.modules.general_schedule.user_interface;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.user_interface.IScheduleItemViewBuilder;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.SchedulePresenter;
import org.openstack.android.summit.modules.general_schedule.IGeneralScheduleWireframe;
import org.openstack.android.summit.modules.general_schedule.business_logic.IGeneralScheduleInteractor;

import java.util.List;

/**
 * Created by Claudio Redi on 12/21/2015.
 */
public class GeneralSchedulePresenter extends SchedulePresenter<GeneralScheduleFragment, IGeneralScheduleInteractor, IGeneralScheduleWireframe> implements IGeneralSchedulePresenter {
    private IScheduleFilter scheduleFilter;

    public GeneralSchedulePresenter(IGeneralScheduleInteractor interactor, IGeneralScheduleWireframe wireframe, IScheduleablePresenter scheduleablePresenter, IScheduleItemViewBuilder scheduleItemViewBuilder, IScheduleFilter scheduleFilter) {
        super(interactor, wireframe, scheduleablePresenter, scheduleItemViewBuilder);
        this.scheduleFilter = scheduleFilter;
    }

    @Override
    protected List<ScheduleItemDTO> getScheduleEvents(DateTime startDate, DateTime endDate, IGeneralScheduleInteractor interactor) {
        List<ScheduleItemDTO> events = interactor.getScheduleEvents(
                startDate.toDate(),
                endDate.toDate(),
                null,
                null,
                null,
                null,
                null);
        return events;
    }
}