package org.openstack.android.summit.modules.events.user_interface;

import org.openstack.android.summit.common.user_interface.IBasePresenter;

/**
 * Created by Claudio Redi on 12/21/2015.
 */
public interface IEventsPresenter extends IBasePresenter<IEventsView> {

    void showFilterView();

    void clearFilters();
}
