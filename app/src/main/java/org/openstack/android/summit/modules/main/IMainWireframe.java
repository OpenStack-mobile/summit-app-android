package org.openstack.android.summit.modules.main;

import org.openstack.android.summit.common.user_interface.IBaseView;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public interface IMainWireframe {

    void showEventsView(IBaseView context);

    void showEventsView(IBaseView context, int day);

    void showEventsViewByLevel(String level, IBaseView context);

    void showEventsViewByTrack(int trackId, IBaseView context);

    void showMyProfileView(IBaseView context, String defaultTabTitle);

    void showSpeakerListView(IBaseView context);

    void showNotificationsListView(IBaseView context);

    void showSearchView(String searchTerm, IBaseView context);

    void showVenuesView(IBaseView context);

    void showAboutView(IBaseView context);

    void showEventDetail(int eventId, IBaseView context);

    void showEventDetail(int eventId, int day, IBaseView context);

    void showSpeakerProfile(int speakerId, IBaseView context);

    void showPushNotification(int pushNotificationId, IBaseView context);

    void showSettingsView(IBaseView context);
}
