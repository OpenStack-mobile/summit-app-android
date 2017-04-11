package org.openstack.android.summit.modules.main.user_interface;

import android.content.Intent;
import android.content.res.Configuration;

import org.openstack.android.summit.common.user_interface.IBasePresenter;
import org.openstack.android.summit.modules.main.exceptions.MissingMemberException;

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

    void onLoggedIn() throws MissingMemberException;

    void onConfigurationChanged(Configuration newConfig);

    void onOpenedNavigationMenu();

    void showEventView(int eventId);

    void showNotificationView();

    void enableDataUpdateService();

    void disableDataUpdateService();

    void onClickLoginButton();

    void onClickMemberProfilePic();

    void onActivityResult(int requestCode, int resultCode, Intent data);
}
