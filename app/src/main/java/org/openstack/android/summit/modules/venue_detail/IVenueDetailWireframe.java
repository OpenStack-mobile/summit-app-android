package org.openstack.android.summit.modules.venue_detail;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.VenueDTO;
import org.openstack.android.summit.common.IBaseWireframe;
import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public interface IVenueDetailWireframe extends IBaseWireframe {

    void presentVenueDetailView(NamedDTO venue, IBaseView context);

    void showVenueMapView(VenueDTO venueDTO, IBaseView context);
}
