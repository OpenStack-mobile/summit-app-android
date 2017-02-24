package org.openstack.android.summit.dagger.modules;

import org.openstack.android.summit.common.INavigationParametersStore;
import org.openstack.android.summit.common.utils.IAppLinkRouter;
import org.openstack.android.summit.modules.rsvp.IRSVPWireframe;
import org.openstack.android.summit.modules.rsvp.RSVPWireframe;

import dagger.Module;
import dagger.Provides;

/**
 * Created by smarcet on 2/23/17.
 */
@Module
public class RSVPModule {

    @Provides
    IRSVPWireframe providesRSVPWireframe(IAppLinkRouter appLinkRouter, INavigationParametersStore navigationParametersStore){
        return new RSVPWireframe(appLinkRouter, navigationParametersStore);
    }
}
