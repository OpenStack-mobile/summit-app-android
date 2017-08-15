package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.IEventTypeDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitTypeDataStore;
import org.openstack.android.summit.common.data_access.repositories.ITagDataStore;
import org.openstack.android.summit.common.data_access.repositories.ITrackGroupDataStore;
import org.openstack.android.summit.common.data_access.repositories.IVenueDataStore;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.modules.general_schedule_filter.GeneralScheduleFilterWireframe;
import org.openstack.android.summit.modules.general_schedule_filter.IGeneralScheduleFilterWireframe;
import org.openstack.android.summit.modules.general_schedule_filter.business_logic.GeneralScheduleFilterInteractor;
import org.openstack.android.summit.modules.general_schedule_filter.business_logic.IGeneralScheduleFilterInteractor;
import org.openstack.android.summit.modules.general_schedule_filter.user_interface.GeneralScheduleFilterFragment;
import org.openstack.android.summit.modules.general_schedule_filter.user_interface.GeneralScheduleFilterPresenter;
import org.openstack.android.summit.modules.general_schedule_filter.user_interface.IGeneralScheduleFilterPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 2/2/2016.
 */
@Module
public class GeneralScheduleFilterModule {
    @Provides
    GeneralScheduleFilterFragment providesGeneralScheduleFilterFragment() {
        return new GeneralScheduleFilterFragment();
    }

    @Provides
    IGeneralScheduleFilterWireframe providesGeneralScheduleFilterWireframe(INavigationParametersStore navigationParametersStore) {
        return new GeneralScheduleFilterWireframe(navigationParametersStore);
    }

    @Provides
    IGeneralScheduleFilterInteractor providesGeneralScheduleFilterInteractor
    (
        ISecurityManager securityManager,
        ISummitDataStore summitDataStore,
        ISummitEventDataStore summitEventDataStore,
        ISummitTypeDataStore summitTypeDataStore,
        IEventTypeDataStore eventTypeDataStore,
        ITagDataStore tagDataStore,
        ITrackGroupDataStore trackGroupDataStore,
        IVenueDataStore venueDataStore,
        IDTOAssembler dtoAssembler,
        ISummitSelector summitSelector,
        IReachability reachability
    ) {
        return new GeneralScheduleFilterInteractor
        (
            securityManager,
            summitDataStore,
            summitEventDataStore,
            venueDataStore,
            trackGroupDataStore,
            summitTypeDataStore,
            eventTypeDataStore,
            tagDataStore,
            dtoAssembler,
            summitSelector,
            reachability
        );
    }

    @Provides
    IGeneralScheduleFilterPresenter providesGeneralScheduleFilterPresenter(IGeneralScheduleFilterInteractor interactor, IGeneralScheduleFilterWireframe wireframe, IScheduleFilter scheduleFilter) {
        return new GeneralScheduleFilterPresenter(interactor, wireframe, scheduleFilter);
    }
}
