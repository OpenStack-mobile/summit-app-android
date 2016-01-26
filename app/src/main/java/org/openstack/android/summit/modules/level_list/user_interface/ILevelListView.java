package org.openstack.android.summit.modules.level_list.user_interface;

import org.openstack.android.summit.common.user_interface.IBaseView;

import java.util.List;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public interface ILevelListView extends IBaseView {
    void setLevels(List<String> levels);

    void reloadData();
}
