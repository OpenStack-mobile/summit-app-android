package org.openstack.android.summit.modules.inbox.user_interface;

import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.inbox.IInboxWireframe;
import org.openstack.android.summit.modules.inbox.business_logic.IInboxInteractor;

/**
 * Created by smarcet on 2/7/17.
 */

public class InboxPresenter
        extends BasePresenter<IInboxView, IInboxInteractor, IInboxWireframe>
        implements IInboxPresenter {

    public InboxPresenter(IInboxInteractor interactor, IInboxWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public int getTabsToShow() {
        /*return interactor.isLoggedIn() ? 2 : 1;*/
        return 1;
    }
}
