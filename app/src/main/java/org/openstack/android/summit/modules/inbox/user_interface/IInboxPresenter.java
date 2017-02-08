package org.openstack.android.summit.modules.inbox.user_interface;

import org.openstack.android.summit.common.user_interface.IBasePresenter;

/**
 * Created by smarcet on 2/7/17.
 */

public interface IInboxPresenter extends IBasePresenter<IInboxView> {
    int getTabsToShow();
}
