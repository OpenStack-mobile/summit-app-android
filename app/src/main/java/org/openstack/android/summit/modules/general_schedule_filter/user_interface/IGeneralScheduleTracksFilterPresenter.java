package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

import org.openstack.android.summit.common.user_interface.IBasePresenter;

/**
 * Created by smarcet on 2/5/18.
 */

public interface IGeneralScheduleTracksFilterPresenter extends IBasePresenter<IGeneralScheduleTracksFilterView> {

    void toggleSelectionTrack(IGeneralScheduleFilterItemView item, int position);

    void buildTrackFilterItem(GeneralScheduleFilterItemView item, int position);
}
