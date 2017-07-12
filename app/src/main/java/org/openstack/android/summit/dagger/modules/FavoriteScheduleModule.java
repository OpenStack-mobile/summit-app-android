package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.IMemberDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.ScheduleItemViewBuilder;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.favorites_schedule.FavoritesScheduleWireframe;
import org.openstack.android.summit.modules.favorites_schedule.IFavoritesScheduleWireframe;
import org.openstack.android.summit.modules.favorites_schedule.business_logic.FavoritesScheduleInteractor;
import org.openstack.android.summit.modules.favorites_schedule.business_logic.IFavoritesScheduleInteractor;
import org.openstack.android.summit.modules.favorites_schedule.user_interface.FavoritesScheduleFragment;
import org.openstack.android.summit.modules.favorites_schedule.user_interface.FavoritesSchedulePresenter;
import org.openstack.android.summit.modules.favorites_schedule.user_interface.IFavoritesSchedulePresenter;
import org.openstack.android.summit.modules.rsvp.IRSVPWireframe;

import dagger.Module;
import dagger.Provides;

/**
 * Created by smarcet on 3/14/17.
 */

@Module
public class FavoriteScheduleModule {

    @Provides
    FavoritesScheduleFragment providesFavoritesScheduleFragment(){
        return new FavoritesScheduleFragment();
    }

    @Provides
    IFavoritesScheduleInteractor providesFavoritesScheduleInteractor (
            IMemberDataStore memberDataStore,
            ISummitEventDataStore summitEventDataStore,
            ISummitDataStore summitDataStore,
            ISummitAttendeeDataStore summitAttendeeDataStore,
            IDTOAssembler dtoAssembler,
            ISecurityManager securityManager,
            IPushNotificationsManager pushNotificationsManager,
            ISession session,
            ISummitSelector summitSelector
    )
    {
        return new FavoritesScheduleInteractor
                   (
                           memberDataStore,
                           summitEventDataStore,
                           summitDataStore,
                           dtoAssembler,
                           securityManager,
                           pushNotificationsManager,
                           session,
                           summitSelector
                   );
    }

    @Provides
    IFavoritesScheduleWireframe providesFavoritesScheduleWireframe
            (
                    IEventDetailWireframe eventDetailWireframe,
                    IRSVPWireframe rsvpWireframe,
                    INavigationParametersStore navigationParametersStore
            )
    {
        return new FavoritesScheduleWireframe(eventDetailWireframe, rsvpWireframe, navigationParametersStore);
    }

    @Provides
    IFavoritesSchedulePresenter providesFavoritesSchedulePresenter(IFavoritesScheduleInteractor interactor, IFavoritesScheduleWireframe wireframe, IScheduleablePresenter scheduleablePresenter, IScheduleFilter scheduleFilter) {
        return new FavoritesSchedulePresenter(interactor, wireframe, scheduleablePresenter, new ScheduleItemViewBuilder(), scheduleFilter);
    }
}
