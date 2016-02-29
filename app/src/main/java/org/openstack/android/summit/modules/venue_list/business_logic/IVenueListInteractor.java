package org.openstack.android.summit.modules.venue_list.business_logic;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.VenueDTO;
import org.openstack.android.summit.common.business_logic.IBaseInteractor;

import java.util.List;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public interface IVenueListInteractor extends IBaseInteractor {
    List<VenueDTO> getInternalVenues();

    List<VenueDTO> getExternalVenues();
}
