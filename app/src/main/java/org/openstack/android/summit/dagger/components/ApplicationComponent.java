package org.openstack.android.summit.dagger.components;

/**
 * Created by claudio on 11/3/2015.
 */

import org.openstack.android.summit.InitialDataLoadingActivity;
import org.openstack.android.summit.common.data_updates.DataUpdatesService;
import org.openstack.android.summit.common.player.YouTubePlayerActivity;
import org.openstack.android.summit.common.push_notifications.CustomParsePushBroadcastReceiver;
import org.openstack.android.summit.common.security.AuthenticatorActivity;
import org.openstack.android.summit.common.security.AuthenticatorService;
import org.openstack.android.summit.dagger.modules.AboutModule;
import org.openstack.android.summit.dagger.modules.FeedbackEditModule;
import org.openstack.android.summit.dagger.modules.MainModule;
import org.openstack.android.summit.dagger.modules.MemberOrderConfirmModule;
import org.openstack.android.summit.dagger.modules.PushNotificationsInboxModule;
import org.openstack.android.summit.dagger.modules.VenueMapModule;
import org.openstack.android.summit.modules.about.user_interface.AboutFragment;
import org.openstack.android.summit.modules.event_detail.user_interface.VenueRoomDetailFragment;
import org.openstack.android.summit.modules.feedback_edit.user_interface.FeedbackEditFragment;
import org.openstack.android.summit.modules.main.user_interface.MainActivity;
import org.openstack.android.summit.dagger.modules.ApplicationModule;
import org.openstack.android.summit.dagger.modules.BaseModule;
import org.openstack.android.summit.dagger.modules.DTOAssemblerModule;
import org.openstack.android.summit.dagger.modules.DataAccessModule;
import org.openstack.android.summit.dagger.modules.EventDetailModule;
import org.openstack.android.summit.dagger.modules.EventsModule;
import org.openstack.android.summit.dagger.modules.FeedbackGivenListModule;
import org.openstack.android.summit.dagger.modules.GeneralScheduleFilterModule;
import org.openstack.android.summit.dagger.modules.GeneralScheduleModule;
import org.openstack.android.summit.dagger.modules.LevelListModule;
import org.openstack.android.summit.dagger.modules.LevelScheduleModule;
import org.openstack.android.summit.dagger.modules.MemberProfileDetailModule;
import org.openstack.android.summit.dagger.modules.MemberProfileModule;
import org.openstack.android.summit.dagger.modules.PersonalScheduleModule;
import org.openstack.android.summit.dagger.modules.ScheduleableModule;
import org.openstack.android.summit.dagger.modules.SearchModule;
import org.openstack.android.summit.dagger.modules.SecurityModule;
import org.openstack.android.summit.dagger.modules.SpeakerListModule;
import org.openstack.android.summit.dagger.modules.SpeakerPresentationsModule;
import org.openstack.android.summit.dagger.modules.TrackListModule;
import org.openstack.android.summit.dagger.modules.TrackScheduleModule;
import org.openstack.android.summit.dagger.modules.VenueDetailModule;
import org.openstack.android.summit.dagger.modules.VenueListModule;
import org.openstack.android.summit.dagger.modules.VenuesMapModule;
import org.openstack.android.summit.dagger.modules.VenuesModule;
import org.openstack.android.summit.modules.event_detail.user_interface.EventDetailFragment;
import org.openstack.android.summit.modules.events.user_interface.EventsFragment;
import org.openstack.android.summit.modules.feedback_given_list.user_interface.FeedbackGivenListFragment;
import org.openstack.android.summit.modules.general_schedule.user_interface.GeneralScheduleFragment;
import org.openstack.android.summit.modules.general_schedule_filter.user_interface.GeneralScheduleFilterFragment;
import org.openstack.android.summit.modules.level_list.user_interface.LevelListFragment;
import org.openstack.android.summit.modules.level_schedule.user_interface.LevelScheduleFragment;
import org.openstack.android.summit.modules.member_order_confirm.user_interface.MemberOrderConfirmFragment;
import org.openstack.android.summit.modules.member_profile.user_interface.MemberProfileFragment;
import org.openstack.android.summit.modules.member_profile_detail.user_interface.MemberProfileDetailFragment;
import org.openstack.android.summit.modules.push_notifications_inbox.user_interface.PushNotificationDetailFragment;
import org.openstack.android.summit.modules.push_notifications_inbox.user_interface.PushPushNotificationsListFragment;
import org.openstack.android.summit.modules.personal_schedule.user_interface.PersonalScheduleFragment;
import org.openstack.android.summit.modules.search.user_interface.SearchFragment;
import org.openstack.android.summit.modules.speaker_presentations.user_interface.SpeakerPresentationsFragment;
import org.openstack.android.summit.modules.speakers_list.user_interface.SpeakerListFragment;
import org.openstack.android.summit.modules.track_list.user_interface.TrackListFragment;
import org.openstack.android.summit.modules.track_schedule.user_interface.TrackScheduleFragment;
import org.openstack.android.summit.modules.venue_detail.user_interface.VenueDetailFragment;
import org.openstack.android.summit.modules.venue_list.user_interface.VenueListFragment;
import org.openstack.android.summit.modules.venue_map.user_interface.VenueMapFragment;
import org.openstack.android.summit.modules.venues.user_interface.VenuesFragment;
import org.openstack.android.summit.modules.venues_map.user_interface.VenuesMapFragment;

import javax.inject.Singleton;
import dagger.Component;

@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = { ApplicationModule.class, MainModule.class, DataAccessModule.class, EventsModule.class, EventDetailModule.class, DTOAssemblerModule.class, SecurityModule.class, GeneralScheduleModule.class, TrackListModule.class, LevelListModule.class, LevelScheduleModule.class, TrackScheduleModule.class, SpeakerListModule.class, SearchModule.class, ScheduleableModule.class, BaseModule.class, EventDetailModule.class, MemberProfileModule.class, PersonalScheduleModule.class, MemberProfileDetailModule.class, SpeakerPresentationsModule.class, FeedbackGivenListModule.class, GeneralScheduleFilterModule.class, VenuesModule.class, VenueListModule.class, VenuesMapModule.class, VenueDetailModule.class, VenueMapModule.class, FeedbackEditModule.class, MemberOrderConfirmModule.class, AboutModule.class, PushNotificationsInboxModule.class} )
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);
    void inject(InitialDataLoadingActivity initialDataLoadingActivity);
    void inject(EventsFragment eventsFragment);
    void inject(GeneralScheduleFragment generalScheduleFragment);
    void inject(LevelListFragment levelListFragment);
    void inject(LevelScheduleFragment levelScheduleFragment);
    void inject(TrackListFragment trackListFragment);
    void inject(TrackScheduleFragment trackScheduleFragment);
    void inject(SpeakerListFragment speakerListFragment);
    void inject(SearchFragment searchFragment);
    void inject(EventDetailFragment eventDetailFragment);
    void inject(MemberProfileFragment memberProfileFragment);
    void inject(MemberProfileDetailFragment memberProfileDetailFragment);
    void inject(PersonalScheduleFragment personalScheduleFragment);
    void inject(SpeakerPresentationsFragment speakerPresentationsFragment);
    void inject(FeedbackGivenListFragment feedbackGivenListFragment);
    void inject(GeneralScheduleFilterFragment generalScheduleFilterFragment);
    void inject(VenuesFragment venuesFragment);
    void inject(VenueListFragment venueListFragment);
    void inject(VenuesMapFragment venuesMapFragment);
    void inject(VenueDetailFragment venueDetailFragment);
    void inject(VenueRoomDetailFragment venueRoomDetailFragment);
    void inject(VenueMapFragment venueMapFragment);
    void inject(FeedbackEditFragment feedbackEditFragment);
    void inject(MemberOrderConfirmFragment memberOrderConfirmFragment);
    void inject(AboutFragment aboutFragment);
    void inject(AuthenticatorService authenticatorService);
    void inject(DataUpdatesService dataUpdatesService);
    void inject(AuthenticatorActivity authenticatorActivity);
    void inject(YouTubePlayerActivity youTubePlayerActivity);
    void inject(CustomParsePushBroadcastReceiver customParsePushBroadcastReceiver);
    void inject(PushPushNotificationsListFragment pushPushNotificationsListFragment);
    void inject(PushNotificationDetailFragment pushNotificationDetailFragment);
}