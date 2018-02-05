package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

import java.util.List;

/**
 * Created by smarcet on 2/5/18.
 */

public interface IGeneralScheduleFilterItemNavigationView extends IGeneralScheduleFilterItemView {
    void setSelectedSubItemsText(List<String> selectedSubItems);
}
