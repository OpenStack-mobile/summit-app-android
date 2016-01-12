package org.openstack.android.summit.modules.level_list.user_interface;

import org.openstack.android.summit.common.user_interface.IBaseFragment;

import java.util.List;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public interface ILevelListFragment extends IBaseFragment {
    void setLevels(List<String> levels);

    void reloadData();
}
