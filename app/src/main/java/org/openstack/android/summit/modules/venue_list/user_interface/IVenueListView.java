package org.openstack.android.summit.modules.venue_list.user_interface;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.VenueDTO;
import org.openstack.android.summit.common.user_interface.IBaseView;

import java.util.List;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public interface IVenueListView extends IBaseView {
    void setInternalVenues(List<VenueDTO> venues);

    void setExternalVenues(List<VenueDTO> venues);
}
