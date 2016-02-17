package org.openstack.android.summit.modules.venue_list;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.venue_detail.IVenueDetailWireframe;
import org.openstack.android.summit.modules.venue_detail.VenueDetailWireframe;
import org.openstack.android.summit.modules.venue_detail.user_interface.VenueDetailFragment;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenueListWireframe implements IVenueListWireframe {
    IVenueDetailWireframe venueDetailWireframe;

    public VenueListWireframe(IVenueDetailWireframe venueDetailWireframe) {
        this.venueDetailWireframe = venueDetailWireframe;
    }

    @Override
    public void showVenueDetail(NamedDTO venue, IBaseView view) {
        venueDetailWireframe.presentVenueDetailView(venue, view);
    }
}
