package org.openstack.android.summit.modules.venues_map.business_logic;

import org.openstack.android.summit.common.DTOs.VenueListItemDTO;
import org.openstack.android.summit.common.business_logic.IBaseInteractor;

import java.util.List;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public interface IVenuesMapInteractor extends IBaseInteractor {
    List<VenueListItemDTO> getInternalVenuesWithCoordinates();
}
