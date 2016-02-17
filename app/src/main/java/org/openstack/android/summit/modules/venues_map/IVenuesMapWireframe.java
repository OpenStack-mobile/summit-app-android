package org.openstack.android.summit.modules.venues_map;

import org.openstack.android.summit.common.DTOs.VenueDTO;
import org.openstack.android.summit.common.DTOs.VenueListItemDTO;
import org.openstack.android.summit.common.IBaseWireframe;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.venues_map.user_interface.IVenuesMapView;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public interface IVenuesMapWireframe extends IBaseWireframe {
    void presentVenueMapView(VenueDTO venue, IBaseView context);

    void showVenueDetailView(VenueListItemDTO venue, IBaseView view);
}
