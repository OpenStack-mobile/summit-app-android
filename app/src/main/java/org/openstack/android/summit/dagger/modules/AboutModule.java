package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.modules.about.AboutWireframe;
import org.openstack.android.summit.modules.about.IAboutWireframe;
import org.openstack.android.summit.modules.about.business_logic.AboutInteractor;
import org.openstack.android.summit.modules.about.business_logic.IAboutInteractor;
import org.openstack.android.summit.modules.about.user_interface.AboutFragment;
import org.openstack.android.summit.modules.about.user_interface.AboutPresenter;
import org.openstack.android.summit.modules.about.user_interface.IAboutPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Claudio Redi on 4/2/2016.
 */
@Module
public class AboutModule {
    @Provides
    AboutFragment providesAboutFragment() {
        return new AboutFragment();
    }

    @Provides
    IAboutWireframe providesAboutWireframe() {
        return new AboutWireframe();
    }

    @Provides
    IAboutInteractor providesAboutInteractor
    (
        ISummitDataStore summitDataStore,
        IDTOAssembler dtoAssembler,
        ISummitSelector summitSelector
    )
    {
        return new AboutInteractor(summitDataStore, dtoAssembler, summitSelector);
    }

    @Provides
    IAboutPresenter providesAboutPresenter(IAboutInteractor aboutInteractor, IAboutWireframe aboutWireframe) {
        return new AboutPresenter(aboutInteractor, aboutWireframe);
    }    
}
