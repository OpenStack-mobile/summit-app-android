package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.modules.event_detail.user_interface.IVenueRoomDetailPresenter;
import org.openstack.android.summit.modules.event_detail.user_interface.VenueRoomDetailPresenter;
import org.openstack.android.summit.modules.venue_detail.IVenueDetailWireframe;
import org.openstack.android.summit.modules.venue_detail.VenueDetailWireframe;
import org.openstack.android.summit.modules.venue_detail.business_logic.IVenueDetailInteractor;
import org.openstack.android.summit.modules.venue_detail.business_logic.VenueDetailInteractor;
import org.openstack.android.summit.modules.venue_detail.user_interface.IVenueDetailPresenter;
import org.openstack.android.summit.modules.venue_detail.user_interface.VenueDetailFragment;
import org.openstack.android.summit.modules.venue_detail.user_interface.VenueDetailPresenter;
import org.openstack.android.summit.modules.venue_map.IVenueMapWireframe;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
@Module
public class VenueDetailModule {
    @Provides
    VenueDetailFragment providesVenueDetailFragment() {
        return new VenueDetailFragment();
    }

    @Provides
    IVenueDetailWireframe providesVenueDetailWireframe(IVenueMapWireframe venueMapWireframe, INavigationParametersStore navigationParametersStore) {
        return new VenueDetailWireframe(venueMapWireframe, navigationParametersStore);
    }

    @Provides
    IVenueDetailInteractor providesVenueDetailInteractor(IGenericDataStore genericDataStore, IDTOAssembler dtoAssembler) {
        return new VenueDetailInteractor(genericDataStore, dtoAssembler);
    }

    @Provides
    IVenueDetailPresenter providesVenueDetailPresenter(IVenueDetailInteractor interactor, IVenueDetailWireframe wireframe) {
        return new VenueDetailPresenter(interactor, wireframe);
    }

    @Provides
    IVenueRoomDetailPresenter providesLocationDetailPresenter(IVenueDetailInteractor interactor, IVenueDetailWireframe wireframe) {
        return new VenueRoomDetailPresenter(interactor, wireframe);
    }
} 