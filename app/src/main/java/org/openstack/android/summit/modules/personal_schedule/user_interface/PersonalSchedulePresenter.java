package org.openstack.android.summit.modules.personal_schedule.user_interface;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.common.user_interface.IScheduleItemViewBuilder;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.SchedulePresenter;
import org.openstack.android.summit.modules.personal_schedule.IPersonalScheduleWireframe;
import org.openstack.android.summit.modules.personal_schedule.business_logic.IPersonalScheduleInteractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public class PersonalSchedulePresenter extends SchedulePresenter<IPersonalScheduleView, IPersonalScheduleInteractor, IPersonalScheduleWireframe> implements IPersonalSchedulePresenter {

    public PersonalSchedulePresenter(IPersonalScheduleInteractor interactor, IPersonalScheduleWireframe wireframe, IScheduleablePresenter scheduleablePresenter, IScheduleItemViewBuilder scheduleItemViewBuilder, IScheduleFilter scheduleFilter) {
        super(interactor, wireframe, scheduleablePresenter, scheduleItemViewBuilder, scheduleFilter);
    }

    @Override
    protected List<ScheduleItemDTO> getScheduleEvents(DateTime startDate, DateTime endDate, IPersonalScheduleInteractor interactor) {
        List<ScheduleItemDTO> events = null;
        if (interactor.isMemberLoggedIn()) {
            events = interactor.getCurrentMemberScheduledEvents(startDate.toDate(), endDate.toDate());
        }
        else {
            events = new ArrayList<>();
        }
        return events;
    }

    @Override
    protected List<DateTime> getDatesWithoutEvents(DateTime startDate, DateTime endDate) {
        List<DateTime> inactiveDates = null;
        if (interactor.isMemberLoggedIn()) {
            inactiveDates = interactor.getCurrentMemberScheduleDatesWithoutEvents(startDate, endDate);
        }
        else {
            inactiveDates = new ArrayList<>();
        }
        return inactiveDates;
    }
}
