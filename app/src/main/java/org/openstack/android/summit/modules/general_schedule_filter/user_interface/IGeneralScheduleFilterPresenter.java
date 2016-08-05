package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

import org.openstack.android.summit.common.user_interface.IBasePresenter;

/**
 * Created by Claudio Redi on 2/3/2016.
 */
public interface IGeneralScheduleFilterPresenter extends IBasePresenter<IGeneralScheduleFilterView> {

    void buildSummitTypeFilterItem(GeneralScheduleFilterItemView item, int position);

    void buildEventTypeFilterItem(GeneralScheduleFilterItemView item, int position);

    void buildLevelFilterItem(GeneralScheduleFilterItemView item, int position);

    void buildTrackGroupFilterItem(GeneralScheduleFilterItemView item, int position);

    void toggleSelectionSummitType(IGeneralScheduleFilterItemView item, int position);

    void toggleSelectionEventType(IGeneralScheduleFilterItemView item, int position);

    void toggleSelectionLevel(IGeneralScheduleFilterItemView item, int position);

    void toggleSelectionTrackGroup(IGeneralScheduleFilterItemView item, int position);

    void addTag(String tag);

    void removeTag(String tagToDelete);

    void toggleHidePastTalks(boolean hidePastTalks);
}
