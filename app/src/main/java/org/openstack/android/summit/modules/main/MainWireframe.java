package org.openstack.android.summit.modules.main;

import org.openstack.android.summit.common.user_interface.FragmentBackStackHelper;
import org.openstack.android.summit.common.user_interface.IBaseView;
import org.openstack.android.summit.modules.about.IAboutWireframe;
import org.openstack.android.summit.modules.events.IEventsWireframe;
import org.openstack.android.summit.modules.member_profile.IMemberProfileWireframe;
import org.openstack.android.summit.modules.push_notifications_inbox.IPushNotificationsWireframe;
import org.openstack.android.summit.modules.search.ISearchWireframe;
import org.openstack.android.summit.modules.settings.ISettingsWireframe;
import org.openstack.android.summit.modules.speakers_list.ISpeakerListWireframe;
import org.openstack.android.summit.modules.venues.IVenuesWireframe;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public class MainWireframe implements IMainWireframe {

    IEventsWireframe eventsWireframe;

    ISpeakerListWireframe speakerListWireframe;

    IMemberProfileWireframe memberProfileWireframe;

    ISearchWireframe searchWireframe;

    IVenuesWireframe venuesWireframe;

    IAboutWireframe aboutWireframe;

    IPushNotificationsWireframe notificationsWireframe;

    ISettingsWireframe settingsWireframe;

    public MainWireframe(IEventsWireframe eventsWireframe,
                         ISpeakerListWireframe speakerListWireframe,
                         IMemberProfileWireframe memberProfileWireframe,
                         ISearchWireframe searchWireframe,
                         IVenuesWireframe venuesWireframe,
                         IAboutWireframe aboutWireframe,
                         IPushNotificationsWireframe notificationsWireframe,
                         ISettingsWireframe settingsWireframe
                         ) {

        this.eventsWireframe             = eventsWireframe;
        this.speakerListWireframe        = speakerListWireframe;
        this.memberProfileWireframe      = memberProfileWireframe;
        this.searchWireframe             = searchWireframe;
        this.venuesWireframe             = venuesWireframe;
        this.aboutWireframe              = aboutWireframe;
        this.notificationsWireframe      = notificationsWireframe;
        this.settingsWireframe           = settingsWireframe;
    }

    public void showEventsView(IBaseView context) {
        eventsWireframe.presentEventsView(context);
    }

    @Override
    public void showMyProfileView(IBaseView context, String defaultTabTitle) {
        FragmentBackStackHelper.clearAllBackStack(context);
        memberProfileWireframe.presentMyProfileView(context, defaultTabTitle);
    }

    public void showSpeakerListView(IBaseView context) {
        speakerListWireframe.presentSpeakersListView(context);
    }

    @Override
    public void showNotificationsListView(IBaseView context) {
        notificationsWireframe.presentNotificationsListView(context);
    }

    public void showSearchView(String searchTerm, IBaseView context) {
        searchWireframe.presentSearchView(searchTerm, context);
    }

    public void showVenuesView(IBaseView context) {
        venuesWireframe.presentVenuesView(context);
    }

    public void showAboutView(IBaseView context) {
        aboutWireframe.presentAboutView(context);
    }

    @Override
    public void showEventDetail(int eventId, IBaseView context){
        eventsWireframe.presentEventsView(context);
        searchWireframe.showEventDetail(eventId, context);
    }

    @Override
    public void showSpeakerProfile(int speakerId, IBaseView context) {
        speakerListWireframe.presentSpeakersListView(context);
        speakerListWireframe.showSpeakerProfile(speakerId, context);
    }

    @Override
    public void showPushNotification(int pushNotificationId, IBaseView context) {
        notificationsWireframe.presentNotificationsListView(context);
        notificationsWireframe.showNotification(pushNotificationId, context);
    }

    @Override
    public void showSettingsView(IBaseView context) {
        settingsWireframe.presentSettingsView(context);
    }
}
