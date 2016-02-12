package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.modules.venue_detail.IVenueDetailWireframe;
import org.openstack.android.summit.modules.venue_detail.VenueDetailWireframe;
import org.openstack.android.summit.modules.venue_detail.business_logic.IVenueDetailInteractor;
import org.openstack.android.summit.modules.venue_detail.business_logic.VenueDetailInteractor;
import org.openstack.android.summit.modules.venue_detail.user_interface.IVenueDetailPresenter;
import org.openstack.android.summit.modules.venue_detail.user_interface.VenueDetailFragment;
import org.openstack.android.summit.modules.venue_detail.user_interface.VenueDetailPresenter;

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
    IVenueDetailWireframe providesVenueDetailWireframe() {
        return new VenueDetailWireframe();
    }

    @Provides
    IVenueDetailInteractor providesVenueDetailInteractor(IDTOAssembler dtoAssembler) {
        return new VenueDetailInteractor(dtoAssembler);
    }

    @Provides
    IVenueDetailPresenter providesVenueDetailPresenter(IVenueDetailInteractor interactor, IVenueDetailWireframe wireframe) {
        return new VenueDetailPresenter(interactor, wireframe);
    }
} 