package org.openstack.android.summit.modules.venues_map.user_interface;

import org.openstack.android.summit.common.user_interface.IBasePresenter;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public interface IVenuesMapPresenter extends IBasePresenter<IVenuesMapView> {
    void showVenueDetail(int venueId);
}
