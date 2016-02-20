package org.openstack.android.summit.modules.venue_map.user_interface;

import org.openstack.android.summit.common.DTOs.VenueDTO;
import org.openstack.android.summit.common.DTOs.VenueListItemDTO;
import org.openstack.android.summit.common.user_interface.IBaseView;

import java.util.List;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public interface IVenueMapView extends IBaseView {
    void setMarker(VenueListItemDTO venue);
}
