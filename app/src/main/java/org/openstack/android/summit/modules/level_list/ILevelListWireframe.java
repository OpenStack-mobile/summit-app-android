package org.openstack.android.summit.modules.level_list;

import android.support.v4.app.FragmentActivity;

import org.openstack.android.summit.modules.level_list.user_interface.LevelListFragment;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public interface ILevelListWireframe {
    void showLevelSchedule(String level, FragmentActivity context);
}
