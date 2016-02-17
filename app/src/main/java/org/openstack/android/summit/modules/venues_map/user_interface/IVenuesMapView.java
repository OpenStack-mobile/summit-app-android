package org.openstack.android.summit.modules.venues_map.user_interface;

import org.openstack.android.summit.common.DTOs.VenueListItemDTO;
import org.openstack.android.summit.common.user_interface.IBaseView;

import java.util.List;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public interface IVenuesMapView extends IBaseView {
    void addMarkers(List<VenueListItemDTO> venues);
}
