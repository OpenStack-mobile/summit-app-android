package org.openstack.android.summit.modules.general_schedule_filter;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.TrackGroupDTO;
import org.openstack.android.summit.common.IBaseWireframe;
import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by Claudio Redi on 2/2/2016.
 */
public interface IGeneralScheduleFilterWireframe extends IBaseWireframe {

    void presentGeneralScheduleFilterView(IBaseView context);

    void presentGeneralScheduleFilterTrackGroupView(IBaseView context, TrackGroupDTO trackGroupDTO);

    void presentGeneralScheduleFilterVenuesGroupView(IBaseView context, NamedDTO venueDTO);
}
