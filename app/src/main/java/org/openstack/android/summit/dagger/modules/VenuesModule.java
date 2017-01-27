package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
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
    IVenuesInteractor providesVenuesInteractor(IDTOAssembler dtoAssembler, ISummitDataStore summitDataStore, ISummitSelector summitSelector) {
        return new VenuesInteractor(dtoAssembler, summitSelector, summitDataStore);
    }

    @Provides
    IVenuesPresenter providesVenuesPresenter(IVenuesInteractor interactor, IVenuesWireframe wireframe) {
        return new VenuesPresenter(interactor, wireframe);
    }
}
