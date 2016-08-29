package org.openstack.android.summit.modules.personal_schedule.user_interface;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.business_logic.IInteractorAsyncOperationListener;
import org.openstack.android.summit.common.business_logic.InteractorAsyncOperationListener;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.common.user_interface.IScheduleItemView;
import org.openstack.android.summit.common.user_interface.IScheduleItemViewBuilder;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.SchedulePresenter;
import org.openstack.android.summit.modules.events.user_interface.IEventsPresenter;
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
        if (interactor.isMemberLoggedAndConfirmedAttendee()) {
            events = interactor.getCurrentMemberScheduledEvents(startDate.toDate(), endDate.toDate());
        }
        else {
            events = new ArrayList<>();
        }
        return events;
    }

    @Override
    protected List<DateTime> getDatesWithoutEvents(DateTime startDate, DateTime endDate) {
        return interactor.isMemberLoggedAndConfirmedAttendee()?
                interactor.getCurrentMemberScheduleDatesWithoutEvents(startDate, endDate):
                new ArrayList<DateTime>();
    }

    @Override
    public void toggleScheduleStatus(IScheduleItemView scheduleItemView, final int position) {
        if(dayEvents.size() - 1 < position ) return;
        view.showActivityIndicator();
        ScheduleItemDTO scheduleItemDTO = dayEvents.get(position);
        IInteractorAsyncOperationListener<Void> interactorAsyncOperationListener = new InteractorAsyncOperationListener<Void>() {
            @Override
            public void onError(String message) {
                view.hideActivityIndicator();
                view.showErrorMessage(message);
            }
            @Override
            public void onSucceed(){
                view.hideActivityIndicator();
                view.removeItem(position);
            }
        };
        scheduleablePresenter.toggleScheduledStatusForEvent(scheduleItemDTO, scheduleItemView, interactor, interactorAsyncOperationListener);
    }
}
