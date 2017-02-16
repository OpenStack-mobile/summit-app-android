package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.IVenueDataStore;
import org.openstack.android.summit.common.security.ISecurityManager;
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
    IVenueMapInteractor providesVenueMapInteractor(ISecurityManager securityManager, IVenueDataStore venueDataStore, IDTOAssembler dtoAssembler, ISummitDataStore summitDataStore, ISummitSelector summitSelector) {
        return new VenueMapInteractor(securityManager, venueDataStore, dtoAssembler, summitDataStore, summitSelector);
    }

    @Provides
    IVenueMapPresenter providesVenueMapPresenter(IVenueMapInteractor interactor, IVenueMapWireframe wireframe) {
        return new VenueMapPresenter(interactor, wireframe);
    }
} 
