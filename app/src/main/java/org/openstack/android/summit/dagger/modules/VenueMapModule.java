package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.modules.venue_map.business_logic.VenueMapInteractor;
import org.openstack.android.summit.modules.venue_map.user_interface.IVenueMapPresenter;
import org.openstack.android.summit.modules.venue_map.user_interface.VenueMapFragment;
import org.openstack.android.summit.modules.venue_map.user_interface.VenueMapPresenter;
import org.openstack.android.summit.modules.venue_map.IVenueMapWireframe;
import org.openstack.android.summit.modules.venue_map.VenueMapWireframe;
import org.openstack.android.summit.modules.venue_map.business_logic.IVenueMapInteractor;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 2/17/2016.
 */
@Module
public class VenueMapModule {
    @Provides
    VenueMapFragment providesVenueMapFragment() {
        return new VenueMapFragment();
    }

    @Provides
    IVenueMapWireframe providesVenueMapWireframe(INavigationParametersStore navigationParametersStore) {
        return new VenueMapWireframe(navigationParametersStore);
    }

    @Provides
    IVenueMapInteractor providesVenueMapInteractor(IGenericDataStore genericDataStore, IDTOAssembler dtoAssembler, IDataUpdatePoller dataUpdatePoller) {
        return new VenueMapInteractor(genericDataStore, dtoAssembler, dataUpdatePoller);
    }

    @Provides
    IVenueMapPresenter providesVenueMapPresenter(IVenueMapInteractor interactor, IVenueMapWireframe wireframe) {
        return new VenueMapPresenter(interactor, wireframe);
    }
} 
