package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.data_access.ISummitAttendeeDataStore;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.data_access.ISummitEventDataStore;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.ScheduleItemViewBuilder;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.general_schedule_filter.IGeneralScheduleFilterWireframe;
import org.openstack.android.summit.modules.venues.IVenuesWireframe;
import org.openstack.android.summit.modules.venues.VenuesWireframe;
import org.openstack.android.summit.modules.venues.business_logic.IVenuesInteractor;
import org.openstack.android.summit.modules.venues.business_logic.VenuesInteractor;
import org.openstack.android.summit.modules.venues.user_interface.IVenuesPresenter;
import org.openstack.android.summit.modules.venues.user_interface.VenuesFragment;
import org.openstack.android.summit.modules.venues.user_interface.VenuesPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
@Module
public class VenuesModule {
    @Provides
    VenuesFragment providesVenuesFragment() {
        return new VenuesFragment();
    }

    @Provides
    IVenuesWireframe providesVenuesWireframe() {
        return new VenuesWireframe();
    }

    @Provides
    IVenuesInteractor providesVenuesInteractor(IDTOAssembler dtoAssembler, IDataUpdatePoller dataUpdatePoller) {
        return new VenuesInteractor(dtoAssembler, dataUpdatePoller);
    }

    @Provides
    IVenuesPresenter providesVenuesPresenter(IVenuesInteractor interactor, IVenuesWireframe wireframe) {
        return new VenuesPresenter(interactor, wireframe);
    }
}
