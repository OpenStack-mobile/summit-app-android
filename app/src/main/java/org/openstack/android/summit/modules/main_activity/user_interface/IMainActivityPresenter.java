package org.openstack.android.summit.modules.main_activity.user_interface;

import org.openstack.android.summit.common.user_interface.IBasePresenter;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public interface IMainActivityPresenter extends IBasePresenter<IMainView> {
    void showEventsView();

    void showMyProfileView();

    void showSpeakerListView();

    void showSearchView(String searchTerm);

    void showVenuesView();
}
