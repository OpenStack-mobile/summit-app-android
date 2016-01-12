package org.openstack.android.summit.modules.level_list.user_interface;

import org.openstack.android.summit.common.user_interface.IPresenter;
import org.openstack.android.summit.common.user_interface.ISimpleListItemView;
import org.openstack.android.summit.common.user_interface.ScheduleItemView;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public interface ILevelListPresenter extends IPresenter<LevelListFragment> {
    void showLevelEvents(int position);

    void buildItem(ISimpleListItemView levelListItemView, int position);
}
