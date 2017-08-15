package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.IMemberDataStore;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.push_notifications.IPushNotificationsManager;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.ISession;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.ScheduleItemViewBuilder;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.personal_schedule.IPersonalScheduleWireframe;
import org.openstack.android.summit.modules.personal_schedule.PersonalScheduleWireframe;
import org.openstack.android.summit.modules.personal_schedule.business_logic.IPersonalScheduleInteractor;
import org.openstack.android.summit.modules.personal_schedule.business_logic.PersonalScheduleInteractor;
import org.openstack.android.summit.modules.personal_schedule.user_interface.IPersonalSchedulePresenter;
import org.openstack.android.summit.modules.personal_schedule.user_interface.PersonalScheduleFragment;
import org.openstack.android.summit.modules.personal_schedule.user_interface.PersonalSchedulePresenter;
import org.openstack.android.summit.modules.rsvp.IRSVPWireframe;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
@Module
public class PersonalScheduleModule {
    @Provides
    PersonalScheduleFragment providesPersonalScheduleFragment() {
        return new PersonalScheduleFragment();
    }

    @Provides
    IPersonalScheduleInteractor providesPersonalScheduleInteractor
    (
            IMemberDataStore memberDataStore,
            ISummitEventDataStore summitEventDataStore,
            ISummitDataStore summitDataStore,
            IDTOAssembler dtoAssembler,
            ISecurityManager securityManager,
            IPushNotificationsManager pushNotificationsManager,
            ISession session,
            ISummitSelector summitSelector,
            IReachability reachability
    )
    {
        return new PersonalScheduleInteractor
                   (
                           memberDataStore,
                           summitEventDataStore,
                           summitDataStore,
                           dtoAssembler,
                           securityManager,
                           pushNotificationsManager,
                           session,
                           summitSelector,
                           reachability
                   );
    }

    @Provides
    IPersonalScheduleWireframe providesPersonalScheduleWireframe
    (
        IEventDetailWireframe eventDetailWireframe,
        IRSVPWireframe rsvpWireframe,
        INavigationParametersStore navigationParametersStore
    )
    {
        return new PersonalScheduleWireframe(eventDetailWireframe, rsvpWireframe, navigationParametersStore);
    }

    @Provides
    IPersonalSchedulePresenter providesPersonalSchedulePresenter(IPersonalScheduleInteractor interactor, IPersonalScheduleWireframe wireframe, IScheduleablePresenter scheduleablePresenter, IScheduleFilter scheduleFilter) {
        return new PersonalSchedulePresenter(interactor, wireframe, scheduleablePresenter, new ScheduleItemViewBuilder(), scheduleFilter);
    }
}
