package org.openstack.android.summit.modules.main.user_interface;

import android.content.res.Configuration;

import org.openstack.android.summit.common.user_interface.IBasePresenter;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public interface IMainPresenter extends IBasePresenter<IMainView> {

    void showEventsView();

    void showMyProfileView();

    void showSpeakerListView();

    void showSearchView(String searchTerm);

    void showVenuesView();

    void showAboutView();

    void onLoggedOut();

    void onLoggedIn();

    void onConfigurationChanged(Configuration newConfig);

    boolean isSummitDataLoaded();

    void onOpenedNavigationMenu();

    void showEventView(int eventId);

    void showNotificationView();

    void enableDataUpdateService();

    void disableDataUpdateService();

    void updateNotificationCounter();

    void shouldShowMainView();
}
