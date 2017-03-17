package org.openstack.android.summit.modules.event_detail.user_interface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.crashlytics.android.Crashlytics;
import com.linearlistview.LinearListView;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.R2;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.FeedbackDTO;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.DTOs.VideoDTO;
import org.openstack.android.summit.common.HtmlTextView;
import org.openstack.android.summit.common.player.VideoPlayer;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.FeedbackItemView;
import org.openstack.android.summit.common.user_interface.MenuHelper;
import org.openstack.android.summit.common.user_interface.PersonItemView;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailFragment
        extends BaseFragment<IEventDetailPresenter>
        implements IEventDetailView {

    private View view;
    private Menu contextMenu;
    private Unbinder unbinder;
    private SpeakerListAdapter speakerListAdapter;
    private FeedbackListAdapter feedbackListAdapter;

    @BindView(R2.id.event_detail_action_rate)
    Button buttonRate;

    @BindView(R2.id.event_detail_action_going)
    ToggleButton buttonGoing;

    @BindView(R2.id.event_detail_action_favorite)
    ToggleButton buttonFavorite;

    @BindView(R2.id.event_detail_place_container)
    LinearLayout locationContainer;

    @BindView(R2.id.event_detail_level_container)
    LinearLayout levelContainer;

    @BindView(R2.id.event_detail_speakers_container)
    LinearLayout speakersContainer;

    @BindView(R2.id.download_attachment_container)
    LinearLayout downloadContainer;

    @BindView(R2.id.download_attachment_button)
    TextView downloadAttachmentLink;

    @BindView(R2.id.must_record_view)
    ImageView mustRecordView;

    @BindView(R2.id.event_detail_speakers_list)
    LinearListView speakerList;

    @BindView(R2.id.event_detail_feedback_list)
    LinearListView feedbackList;

    @BindView(R2.id.event_detail_load_more_feedback_button)
    Button loadMoreFeedbackButton;

    @BindView(R2.id.event_feedback_total_qty)
    TextView feedbackQtyTxt;

    @BindView(R2.id.event_detail_name)
    TextView nameTextView;

    @BindView(R2.id.event_detail_track)
    TextView trackTextView;

    @BindView(R2.id.event_detail_description)
    HtmlTextView descriptionTextView;

    @BindView(R2.id.event_detail_date)
    TextView dateTextView;

    @BindView(R2.id.event_detail_time)
    TextView timeTextView;

    @BindView(R2.id.event_detail_place)
    TextView locationTextView;

    @BindView(R2.id.event_detail_level)
    TextView levelTextView;

    @BindView(R2.id.event_detail_sponsors)
    TextView sponsorsTextView;

    @BindView(R2.id.event_detail_tags)
    TextView tagsTextView;

    @BindView(R2.id.event_detail_my_feedback)
    LinearLayout myFeedbackLayout;

    @BindView(R2.id.event_detail_loading_feedback)
    LinearLayout loadingIndicatorLayout;

    @BindView(R2.id.event_detail_feedback_container)
    LinearLayout feedbackContainerLayout;

    @BindView(R2.id.event_detail_error_loading_feedback_text)
    TextView feedbackErrorTextView;

    @BindView(R2.id.item_feedback_review)
    TextView reviewText;

    @BindView(R2.id.item_feedback_date)
    TextView dateText;

    @BindView(R2.id.event_avg_feedback_rate)
    LinearLayout averageFeedbackLayout;

    @BindView(R2.id.event_avg_feedback_rate_1)
    ImageView avgRate1;

    @BindView(R2.id.event_avg_feedback_rate_2)
    ImageView avgRate2;

    @BindView(R2.id.event_avg_feedback_rate_3)
    ImageView avgRate3;

    @BindView(R2.id.event_avg_feedback_rate_4)
    ImageView avgRate4;

    @BindView(R2.id.event_avg_feedback_rate_5)
    ImageView avgRate5;

    @BindView(R2.id.item_feedback_rate_1)
    ImageView myRate1;

    @BindView(R2.id.item_feedback_rate_2)
    ImageView myRate2;

    @BindView(R2.id.item_feedback_rate_3)
    ImageView myRate3;

    @BindView(R2.id.item_feedback_rate_4)
    ImageView myRate4;

    @BindView(R2.id.item_feedback_rate_5)
    ImageView myRate5;

    @BindView(R2.id.event_avg_feedback_val)
    TextView rateVal;

    @BindView(R2.id.video_preview)
    VideoPlayer videoPlayer;

    @BindString(R2.string.download_attachment) String downloadAttachmentString;

    @BindString(R2.string.download_slide) String downloadSlideString;

    public EventDetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        speakerListAdapter = new SpeakerListAdapter(getContext());
        speakerList.setAdapter(speakerListAdapter);

        feedbackListAdapter = new FeedbackListAdapter(getContext());
        feedbackList.setAdapter(feedbackListAdapter);

        loadMoreFeedbackButton.setOnClickListener(v -> presenter.loadFeedback());

        buttonFavorite.setVisibility(View.GONE);

        buttonGoing.setVisibility(View.GONE);

        buttonRate.setVisibility(View.GONE);

        buttonRate.setOnClickListener(v -> presenter.showFeedbackEdit(0));

        speakersContainer.setVisibility(View.GONE);

        locationContainer.setVisibility(View.GONE);
        locationContainer.setOnClickListener(v -> presenter.showVenueDetail());

        levelContainer.setVisibility(View.GONE);
        levelContainer.setOnClickListener(v -> presenter.showEventsByLevel());

        downloadContainer.setVisibility(View.GONE);

        downloadAttachmentLink.setOnClickListener(v -> presenter.openAttachment());

        mustRecordView.setVisibility(View.GONE);

        ((LinearLayout) feedbackErrorTextView.getParent()).setVisibility(View.GONE);

        super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        presenter.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {

                if (intent.getAction() == Constants.DATA_UPDATE_UPDATED_ENTITY_EVENT) {
                    int entityId = intent.getIntExtra(Constants.DATA_UPDATE_ENTITY_ID, 0);
                    String entityClassName = intent.getStringExtra(Constants.DATA_UPDATE_ENTITY_CLASS);
                    Log.d(Constants.LOG_TAG, "DATA_UPDATE_UPDATED_ENTITY_EVENT");
                    if (entityClassName != null && (entityClassName.equals("Presentation") || entityClassName.equals("SummitEvent")))
                        presenter.updateUI();
                }

            } catch (Exception ex) {
                Crashlytics.logException(new Exception(String.format("Action %s", intent.getAction()), ex));
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
        setTitle(getResources().getString(R.string.event));
        // bind local broadcast receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.DATA_UPDATE_UPDATED_ENTITY_EVENT);
        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).registerReceiver(messageReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
        LocalBroadcastManager.getInstance(OpenStackSummitApplication.context).unregisterReceiver(messageReceiver);
    }

    @Override
    public void loadVideo(VideoDTO video) {
        if (videoPlayer == null) return;
        if (video == null) return;

        videoPlayer.loadVideo(video);
    }

    @Override
    public void showGoingButton(boolean show) {
        buttonGoing.setVisibility(show ? View.VISIBLE : view.GONE);
    }

    @Override
    public void showFavoriteButton(boolean show) {
        buttonFavorite.setVisibility(show ? View.VISIBLE : view.GONE);
    }

    @Override
    public void showRateButton(boolean show) {
        buttonRate.setVisibility(show ? View.VISIBLE : view.GONE);
    }

    @Override
    public void setFavoriteButtonState(boolean pressed) {
        buttonFavorite.setOnCheckedChangeListener(null);
        buttonFavorite.setChecked(pressed);
        buttonFavorite.setOnCheckedChangeListener((buttonView, isChecked)  -> presenter.toggleFavoriteStatus());
    }

    @Override
    public void setGoingButtonState(boolean pressed) {
        buttonGoing.setOnCheckedChangeListener(null);
        buttonGoing.setChecked(pressed);
        //could be schedule or RSVP
        buttonGoing.setOnCheckedChangeListener((buttonView, isChecked)  -> presenter.buttonGoingPressed());
    }

    @Override
    public void showAddFavoriteMenuAction(boolean show) {
        if (contextMenu == null) return;
        MenuItem item = contextMenu.findItem(R.id.event_detail_menu_save_favorite_action);
        if (item == null) return;
        item.setVisible(show);
    }

    @Override
    public void showRemoveFavoriteMenuAction(boolean show) {
        if (contextMenu == null) return;
        MenuItem item = contextMenu.findItem(R.id.event_detail_menu_remove_favorite_action);
        if (item == null) return;
        item.setVisible(show);
    }

    @Override
    public void showGoingMenuAction(boolean show) {
        if (contextMenu == null) return;
        MenuItem item = contextMenu.findItem(R.id.event_detail_menu_save_going_action);
        if (item == null) return;
        item.setVisible(show);
    }

    @Override
    public void showNotGoingMenuAction(boolean show) {
        if (contextMenu == null) return;
        MenuItem item = contextMenu.findItem(R.id.event_detail_menu_remove_going_action);
        if (item == null) return;
        item.setVisible(show);
    }

    @Override
    public void showRSVPMenuAction(boolean show) {
        if (contextMenu == null) return;
        MenuItem item = contextMenu.findItem(R.id.event_detail_menu_save_rsvp_action);
        if (item == null) return;
        item.setVisible(show);
    }

    @Override
    public void showRateMenuAction(boolean show) {
        if (contextMenu == null) return;
        MenuItem item = contextMenu.findItem(R.id.event_detail_menu_rate_action);
        if (item == null) return;
        item.setVisible(show);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.contextMenu = menu;
        menu.clear();
        inflater.inflate(R.menu.event_detail, menu);
        MenuHelper.setShowIcons(menu);
        presenter.updateActions();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.event_detail_menu_rate_action:
                presenter.showFeedbackEdit(0);
                return true;
            case R.id.event_detail_menu_save_rsvp_action:
                presenter.toggleRSVPStatus();
                return true;
            case R.id.event_detail_menu_save_favorite_action:
                presenter.toggleFavoriteStatus();
                return true;
            case R.id.event_detail_menu_remove_favorite_action:
                presenter.toggleFavoriteStatus();
                return true;
            case R.id.event_detail_menu_remove_going_action:
                presenter.toggleScheduleStatus();
                return true;
            case R.id.event_detail_menu_save_going_action:
                presenter.toggleScheduleStatus();
                return true;
            case R.id.event_detail_menu_share_action:
                Intent shareIntent = presenter.createShareIntent();
                startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.action_share_event)));
            return true;
            case R.id.event_detail_menu_cancel_action:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onViewStateRestored(savedInstanceState);
        presenter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void setName(String name) {
        if (nameTextView == null) return;
        nameTextView.setText(name);
    }

    @Override
    public void setTrack(String track) {
        if (trackTextView == null) return;
        trackTextView.setVisibility(track != null && !track.isEmpty() ? View.VISIBLE : View.GONE);
        trackTextView.setText(track);
    }

    @Override
    public void setTrackColor(String color) {

        if (color == null || color.length() == 0) {
            trackTextView.setTextColor(getResources().getColor(R.color.white));
        } else {
            trackTextView.setTextColor(Color.parseColor(color));
        }
    }

    @Override
    public void setDescription(String description) {
        if (descriptionTextView == null) return;
        descriptionTextView.setVisibility(description != null && !description.isEmpty() ? View.VISIBLE : View.GONE);
        if (description != null && !description.isEmpty())
            descriptionTextView.setText(description);
    }

    @Override
    public void setDate(String date) {
        if (dateTextView == null) return;
        ((LinearLayout) dateTextView.getParent()).setVisibility(date != null && !date.isEmpty() ? View.VISIBLE : View.GONE);
        dateTextView.setText(date);
    }

    @Override
    public void setTime(String time) {
        if (timeTextView == null) return;
        ((LinearLayout) timeTextView.getParent()).setVisibility(time != null && !time.isEmpty() ? View.VISIBLE : View.GONE);
        timeTextView.setText(time);
    }

    @Override
    public void setLocation(String location) {
        if (locationTextView == null) return;
        if (location == null || location.isEmpty()) {
            locationContainer.setVisibility(View.GONE);
            return;
        }
        locationContainer.setVisibility(View.VISIBLE);

        ((LinearLayout) locationTextView.getParent()).setVisibility(location != null && !location.isEmpty() ? View.VISIBLE : View.GONE);
        locationTextView.setText(location);
    }

    @Override
    public void setLevel(String level) {
        if (levelTextView == null) return;
        if (level == null || level.isEmpty()) {
            levelContainer.setVisibility(View.GONE);
            return;
        }
        levelContainer.setVisibility(View.VISIBLE);

        ((LinearLayout) levelTextView.getParent()).setVisibility(level != null && !level.isEmpty() ? View.VISIBLE : View.GONE);
        levelTextView.setText(level);
    }

    @Override
    public void setSponsors(String sponsors) {
        if (sponsorsTextView == null) return;
        sponsorsTextView.setVisibility(sponsors != null && !sponsors.isEmpty() ? View.VISIBLE : View.GONE);
        sponsorsTextView.setText(sponsors);
    }

    @Override
    public void setTags(String tags) {
        if (tagsTextView == null) return;
        ((LinearLayout) tagsTextView.getParent()).setVisibility(tags != null && !tags.isEmpty() ? View.VISIBLE : View.GONE);
        tagsTextView.setText(tags);
    }

    @Override
    public void setSpeakers(List<PersonListItemDTO> speakers) {

        speakerListAdapter.clear();
        if (speakers.isEmpty()) {
            speakersContainer.setVisibility(View.GONE);
            return;
        }

        speakersContainer.setVisibility(View.VISIBLE);
        speakerListAdapter.addAll(speakers);

        speakerList.setVisibility(speakers.size() > 0 ? View.VISIBLE : View.GONE);
        speakerList.setOnItemClickListener((parent, view1, position, id) -> presenter.showSpeakerProfile(position));
    }

    @Override
    public void setAverageRate(double rate) {

        if(averageFeedbackLayout == null) return;

        if (rate == 0) {
            averageFeedbackLayout.setVisibility(View.GONE);
            return;
        }

        averageFeedbackLayout.setVisibility(View.VISIBLE);
        // star #1
        if(rate > 0 && rate < 1){
            avgRate1.setImageResource(R.drawable.ic_star_half);
        }
        if (rate >= 1) {
            avgRate1.setImageResource(R.drawable.ic_star);
        }
        // star #2
        if(rate > 1 && rate < 2 ){
            avgRate2.setImageResource(R.drawable.ic_star_half);
        }
        if (rate >= 2) {
            avgRate2.setImageResource(R.drawable.ic_star);
        }
        // star #3
        if(rate > 2 && rate < 3 ){
            avgRate3.setImageResource(R.drawable.ic_star_half);
        }
        if (rate >= 3) {
            avgRate3.setImageResource(R.drawable.ic_star);
        }
        // star #4
        if(rate > 3 && rate < 4 ){
            avgRate4.setImageResource(R.drawable.ic_star_half);
        }
        if (rate >= 4) {
            avgRate4.setImageResource(R.drawable.ic_star);
        }
        // star #5
        if(rate > 4 && rate < 5 ){
            avgRate5.setImageResource(R.drawable.ic_star_half);
        }
        if (rate >= 5) {
            avgRate5.setImageResource(R.drawable.ic_star);
        }

        rateVal.setText(String.format("%.1f", rate));
    }

    @Override
    public void setMyFeedbackRate(int rate) {

        if (rate >= 1) {
            myRate1.setImageResource(R.drawable.ic_star);
        }
        if (rate >= 2) {
            myRate2.setImageResource(R.drawable.ic_star);
        }
        if (rate >= 3) {
            myRate3.setImageResource(R.drawable.ic_star);
        }
        if (rate >= 4) {
            myRate4.setImageResource(R.drawable.ic_star);
        }
        if (rate >= 5) {
            myRate5.setImageResource(R.drawable.ic_star);
        }
    }

    @Override
    public void setMyFeedbackReview(String review) {
        if (reviewText == null) return;
        reviewText.setText(review);
    }

    @Override
    public void setMyFeedbackDate(String date) {
        if (dateText == null) return;
        dateText.setText(date);
    }

    @Override
    public void hasMyFeedback(boolean hasMyFeedback) {
        if (myFeedbackLayout == null) return;
        myFeedbackLayout.setVisibility(hasMyFeedback ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showFeedbackActivityIndicator() {
        if (loadingIndicatorLayout == null) return;
        loadingIndicatorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFeedbackContainer() {
        if (feedbackContainerLayout == null) return;

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(2000);

        feedbackContainerLayout.setVisibility(View.VISIBLE);
        feedbackContainerLayout.setAnimation(fadeIn);
    }

    @Override
    public void hideFeedbackActivityIndicator() {
        if (loadingIndicatorLayout == null) return;
        loadingIndicatorLayout.setVisibility(View.GONE);
    }

    @Override
    public void setOtherPeopleFeedback(List<FeedbackDTO> feedback) {
        if (feedbackList == null) return;
        feedbackListAdapter.clear();
        feedbackListAdapter.addAll(feedback);
        feedbackListAdapter.notifyDataSetChanged();
        feedbackList.setVisibility(feedback.size() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setReviewCount(int reviewCount) {
        if (feedbackQtyTxt == null) return;
        feedbackQtyTxt.setText(String.format
                (
                        OpenStackSummitApplication.context.getString(R.string.event_details_reviews),
                        reviewCount
                ));
    }

    @Override
    public void showAttachment(boolean show, boolean isPresentation) {
        if (downloadContainer == null) return;
        downloadAttachmentLink.setText(isPresentation ? downloadSlideString : downloadAttachmentString);
        downloadContainer.setVisibility(show ? View.VISIBLE : view.GONE);
    }

    @Override
    public void showToRecord(boolean show) {
        if (mustRecordView == null) return;
        mustRecordView.setVisibility(show ? View.VISIBLE : view.GONE);
    }

    @Override
    public void toggleLoadMore(boolean show) {
        if (loadMoreFeedbackButton == null) return;
        loadMoreFeedbackButton.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showFeedbackErrorMessage(String message) {
        if (feedbackErrorTextView == null) return;
        feedbackErrorTextView.setText(message);
        ((LinearLayout) feedbackErrorTextView.getParent()).setVisibility(View.VISIBLE);
    }

    private class SpeakerListAdapter extends ArrayAdapter<PersonListItemDTO> {

        public SpeakerListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_person_list, parent, false);
            }

            final PersonItemView personItemView = new PersonItemView(convertView);

            presenter.buildSpeakerListItem(personItemView, position);

            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }
    }

    private class FeedbackListAdapter extends ArrayAdapter<FeedbackDTO> {

        public FeedbackListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_feedback_list, parent, false);
            }

            final FeedbackItemView feedbackItemView = new FeedbackItemView(convertView);

            presenter.buildFeedbackListItem(feedbackItemView, position);

            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        if(unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }

}
