package org.openstack.android.summit.modules.venue_list;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.venue_list.user_interface.IVenueListView;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public interface IVenueListWireframe {
    void showVenueDetail(NamedDTO venue, IBaseView view);
}
