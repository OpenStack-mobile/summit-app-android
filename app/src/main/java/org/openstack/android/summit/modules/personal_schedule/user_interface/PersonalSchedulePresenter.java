package org.openstack.android.summit.modules.personal_schedule.user_interface;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.user_interface.IScheduleItemView;
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
public class PersonalSchedulePresenter
        extends SchedulePresenter<IPersonalScheduleView, IPersonalScheduleInteractor, IPersonalScheduleWireframe>
        implements IPersonalSchedulePresenter {

    public PersonalSchedulePresenter(IPersonalScheduleInteractor interactor, IPersonalScheduleWireframe wireframe, IScheduleablePresenter scheduleablePresenter, IScheduleItemViewBuilder scheduleItemViewBuilder, IScheduleFilter scheduleFilter) {
        super(interactor, wireframe, scheduleablePresenter, scheduleItemViewBuilder, scheduleFilter);

        this.toggleScheduleStatusListener = (position, formerState, viewItem) -> {
            if(formerState && !viewItem.getFavorite()) {
                removeItem(position);
            }
        };

        this.toggleFavoriteStatusListener = (position, formerState, viewItem) -> {
            if(formerState && !viewItem.getScheduled()) {
                removeItem(position);
            }
        };
    }

    @Override
    protected List<ScheduleItemDTO> getScheduleEvents(DateTime startDate, DateTime endDate, IPersonalScheduleInteractor interactor) {
        List<ScheduleItemDTO> events = null;
        if (interactor.isMemberLoggedInAndConfirmedAttendee()) {
            events = interactor.getCurrentMemberScheduledEvents(startDate.toDate(), endDate.toDate());
        }
        else {
            events = new ArrayList<>();
        }
        return events;
    }

    @Override
    protected List<DateTime> getDatesWithoutEvents(DateTime startDate, DateTime endDate) {
        return interactor.isMemberLoggedInAndConfirmedAttendee()?
                interactor.getCurrentMemberScheduleDatesWithoutEvents(startDate, endDate):
                new ArrayList<>();
    }

    @Override
    public void toggleScheduleStatus(IScheduleItemView scheduleItemView, final int position) {
        _toggleScheduleStatus(scheduleItemView, position);
    }

    @Override
    protected ScheduleItemDTO getCurrentItem(int position) {
        if(dayEvents.size() - 1 < position ) return null;
        return dayEvents.get(position);
    }

    private void removeItem(int position){
        if(dayEvents.size() - 1 < position ) return;
        dayEvents.remove(position);
        view.removeItem(position);
    }

    @Override
    public void toggleFavoriteStatus(IScheduleItemView scheduleItemView, int position) {
       _toggleFavoriteStatus(scheduleItemView, position);
    }
}
