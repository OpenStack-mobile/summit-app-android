package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.user_interface.IBaseView;

import java.util.List;

public interface IGeneralScheduleRoomsFilterView extends IBaseView {

    void showRooms(List<NamedDTO> rooms);

    Integer getSelectedVenueId();
}

