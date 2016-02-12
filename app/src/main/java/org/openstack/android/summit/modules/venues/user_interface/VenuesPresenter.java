package org.openstack.android.summit.modules.venues.user_interface;

import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.venues.IVenuesWireframe;
import org.openstack.android.summit.modules.venues.business_logic.IVenuesInteractor;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenuesPresenter extends BasePresenter<IVenuesView, IVenuesInteractor, IVenuesWireframe> implements IVenuesPresenter {
    public VenuesPresenter(IVenuesInteractor interactor, IVenuesWireframe wireframe) {
        super(interactor, wireframe);
    }
}
