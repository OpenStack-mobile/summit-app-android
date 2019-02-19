package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

import org.openstack.android.summit.common.user_interface.IBasePresenter;

public interface IGeneralScheduleRoomsFilterPresenter extends IBasePresenter<IGeneralScheduleRoomsFilterView> {

    void toggleSelectionRooms(IGeneralScheduleFilterItemView item, int position);

    void buildRoomFilterItem(GeneralScheduleFilterItemView item, int position);
}

