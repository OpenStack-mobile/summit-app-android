package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.user_interface.IScheduleItemViewBuilder;
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
    IPersonalScheduleInteractor providesPersonalScheduleInteractor(ISummitEventDataStore summitEventDataStore, ISummitDataStore summitDataStore, ISummitAttendeeDataStore summitAttendeeDataStore, IDTOAssembler dtoAssembler, ISecurityManager securityManager, IDataUpdatePoller dataUpdatePoller) {
        return new PersonalScheduleInteractor(summitEventDataStore, summitDataStore, summitAttendeeDataStore, dtoAssembler, securityManager, dataUpdatePoller);
    }

    @Provides
    IPersonalScheduleWireframe providesPersonalScheduleWireframe(IEventDetailWireframe eventDetailWireframe, INavigationParametersStore navigationParametersStore) {
        return new PersonalScheduleWireframe(eventDetailWireframe, navigationParametersStore);
    }

    @Provides
    IPersonalSchedulePresenter providesPersonalSchedulePresenter(IPersonalScheduleInteractor interactor, IPersonalScheduleWireframe wireframe, IScheduleablePresenter scheduleablePresenter, IScheduleFilter scheduleFilter) {
        return new PersonalSchedulePresenter(interactor, wireframe, scheduleablePresenter, new ScheduleItemViewBuilder(), scheduleFilter);
    }
}
