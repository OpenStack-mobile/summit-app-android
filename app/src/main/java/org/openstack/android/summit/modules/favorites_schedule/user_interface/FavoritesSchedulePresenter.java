package org.openstack.android.summit.modules.favorites_schedule.user_interface;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.user_interface.IScheduleItemView;
import org.openstack.android.summit.common.user_interface.IScheduleItemViewBuilder;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.SchedulePresenter;
import org.openstack.android.summit.modules.favorites_schedule.IFavoritesScheduleWireframe;
import org.openstack.android.summit.modules.favorites_schedule.business_logic.IFavoritesScheduleInteractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smarcet on 3/14/17.
 */

public class FavoritesSchedulePresenter
        extends SchedulePresenter<IFavoritesScheduleView, IFavoritesScheduleInteractor, IFavoritesScheduleWireframe>
        implements IFavoritesSchedulePresenter {

    public FavoritesSchedulePresenter(IFavoritesScheduleInteractor interactor, IFavoritesScheduleWireframe wireframe, IScheduleablePresenter scheduleablePresenter, IScheduleItemViewBuilder scheduleItemViewBuilder, IScheduleFilter scheduleFilter) {
        super(interactor, wireframe, scheduleablePresenter, scheduleItemViewBuilder, scheduleFilter);

        this.toggleFavoriteStatusListener = (position, formerState, viewItem) -> {
            if(formerState) {
                removeItem(position);
            }
        };
    }

    @Override
    protected List<ScheduleItemDTO> getScheduleEvents(DateTime startDate, DateTime endDate, IFavoritesScheduleInteractor interactor) {
        return (interactor.isMemberLoggedIn()) ?
                interactor.getCurrentMemberFavoritesEvents(startDate.toDate(), endDate.toDate()) :
                new ArrayList<>();
    }

    @Override
    protected List<DateTime> getDatesWithoutEvents(DateTime startDate, DateTime endDate) {
        return interactor.isMemberLoggedIn()?
               interactor.getCurrentMemberFavoritesDatesWithoutEvents(startDate, endDate):
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

    @Override
    public void buildItem(IScheduleItemView scheduleItemView, int position) {
        if (dayEvents.size() - 1 < position) return;
        ScheduleItemDTO scheduleItemDTO = dayEvents.get(position);
        scheduleItemViewBuilder.build
                (
                        scheduleItemView,
                        scheduleItemDTO,
                        interactor.isMemberLoggedIn(),
                        interactor.isMemberLoggedInAndConfirmedAttendee(),
                        interactor.isEventScheduledByLoggedMember(scheduleItemDTO.getId()),
                        interactor.isEventFavoriteByLoggedMember(scheduleItemDTO.getId()),
                        false,
                        interactor.shouldShowVenues(),
                        scheduleItemDTO.getRSVPLink() ,
                        scheduleItemDTO.isExternalRSVP(),
                        false,
                        true
                );
    }

    @Override
    public void onResume() {
        this.shouldShowNow = false;
        super.onResume();
    }
}