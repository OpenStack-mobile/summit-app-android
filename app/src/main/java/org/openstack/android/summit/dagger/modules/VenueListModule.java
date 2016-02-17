package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.modules.venue_detail.IVenueDetailWireframe;
import org.openstack.android.summit.modules.venue_list.IVenueListWireframe;
import org.openstack.android.summit.modules.venue_list.VenueListWireframe;
import org.openstack.android.summit.modules.venue_list.business_logic.IVenueListInteractor;
import org.openstack.android.summit.modules.venue_list.business_logic.VenueListInteractor;
import org.openstack.android.summit.modules.venue_list.user_interface.IVenueListPresenter;
import org.openstack.android.summit.modules.venue_list.user_interface.VenueListFragment;
import org.openstack.android.summit.modules.venue_list.user_interface.VenueListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
@Module
public class VenueListModule {
    @Provides
    VenueListFragment providesVenueListFragment() {
        return new VenueListFragment();
    }

    @Provides
    IVenueListWireframe providesVenueListWireframe(IVenueDetailWireframe venueDetailWireframe) {
        return new VenueListWireframe(venueDetailWireframe);
    }

    @Provides
    IVenueListInteractor providesVenueListInteractor(IGenericDataStore genericDataStore, IDTOAssembler dtoAssembler) {
        return new VenueListInteractor(genericDataStore, dtoAssembler);
    }

    @Provides
    IVenueListPresenter providesVenueListPresenter(IVenueListInteractor interactor, IVenueListWireframe wireframe) {
        return new VenueListPresenter(interactor, wireframe);
    }
} 