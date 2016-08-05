package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
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
    IGeneralScheduleFilterInteractor providesGeneralScheduleFilterInteractor(ISummitDataStore summitDataStore, ISummitEventDataStore summitEventDataStore, IGenericDataStore genericDataStore, IDTOAssembler dtoAssembler, IDataUpdatePoller dataUpdatePoller) {
        return new GeneralScheduleFilterInteractor(summitDataStore, summitEventDataStore, genericDataStore, dtoAssembler, dataUpdatePoller);
    }

    @Provides
    IGeneralScheduleFilterPresenter providesGeneralScheduleFilterPresenter(IGeneralScheduleFilterInteractor interactor, IGeneralScheduleFilterWireframe wireframe, IScheduleFilter scheduleFilter) {
        return new GeneralScheduleFilterPresenter(interactor, wireframe, scheduleFilter);
    }
}
