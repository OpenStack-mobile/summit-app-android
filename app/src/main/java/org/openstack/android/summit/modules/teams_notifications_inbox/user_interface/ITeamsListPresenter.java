package org.openstack.android.summit.modules.teams_notifications_inbox.user_interface;

import org.openstack.android.summit.common.user_interface.IBasePresenter;

/**
 * Created by smarcet on 2/13/17.
 */

public interface ITeamsListPresenter extends IBasePresenter<ITeamsListView> {

    void loadData();
}
