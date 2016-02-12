package org.openstack.android.summit.modules.venue_detail.user_interface;

import org.openstack.android.summit.common.business_logic.IBaseInteractor;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.venue_detail.IVenueDetailWireframe;
import org.openstack.android.summit.modules.venue_detail.business_logic.IVenueDetailInteractor;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public class VenueDetailPresenter extends BasePresenter<IVenueDetailView, IVenueDetailInteractor, IVenueDetailWireframe> implements IVenueDetailPresenter {
    public VenueDetailPresenter(IVenueDetailInteractor interactor, IVenueDetailWireframe wireframe) {
        super(interactor, wireframe);
    }
}
