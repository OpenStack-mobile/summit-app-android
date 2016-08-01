package org.openstack.android.summit.modules.event_detail.user_interface;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import com.linearlistview.LinearListView;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.FeedbackDTO;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.FeedbackItemView;
import org.openstack.android.summit.common.user_interface.PersonItemView;
import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailFragment extends BaseFragment<IEventDetailPresenter> implements IEventDetailView {

    private SpeakerListAdapter speakerListAdapter;
    private FeedbackListAdapter feedbackListAdapter;
    private View view;
    private Menu menu;
    private boolean scheduled;
    private boolean isScheduledStatusVisible;
    private boolean allowNewFeedback;
    private boolean allowRsvp;
    private ShareActionProvider shareActionProvider;

    public EventDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(getResources().getString(R.string.event));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event_detail, container, false);
        LinearListView speakerList = (LinearListView)view.findViewById(R.id.event_detail_speakers_list);
        speakerListAdapter = new SpeakerListAdapter(getContext());
        speakerList.setAdapter(speakerListAdapter);

        LinearListView feedbackList = (LinearListView)view.findViewById(R.id.event_detail_feedback_list);
        feedbackListAdapter = new FeedbackListAdapter(getContext());
        feedbackList.setAdapter(feedbackListAdapter);

        Button loadMoreFeedbackButton = (Button)view.findViewById(R.id.event_detail_load_more_feedback_button);
        loadMoreFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadFeedback();
            }
        });

        LinearLayout locationLayout = (LinearLayout)view.findViewById(R.id.event_detail_place_container);
        locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.showVenueDetail();
            }
        });

        TextView feedbackErrorTextView = (TextView)view.findViewById(R.id.event_detail_error_loading_feedback_text);
        ((LinearLayout)feedbackErrorTextView.getParent()).setVisibility(View.GONE);

        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.event_detail, menu);
        this.menu = menu;
        setScheduledInternal(scheduled);
        setIsScheduledStatusVisibleInternal(isScheduledStatusVisible);
        setAllowNewFeedback(allowNewFeedback);
        setIAllowRsvpInternal(allowRsvp);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);
        // Fetch and store ShareActionProvider
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        setShareIntent(presenter.createShareIntent());
        // Return true to display menu
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_scheduled_status) {
            presenter.toggleScheduleStatus();
            return true;
        }
        else if (id == R.id.action_create_feedback) {
            presenter.showFeedbackEdit();
        }
        else if (id == R.id.action_rsvp) {
            presenter.showEventRsvpView();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    @Override
    public void setName(String name) {
        TextView nameTextView = (TextView)view.findViewById(R.id.event_detail_name);
        nameTextView.setText(name);
    }

    @Override
    public void setTrack(String track) {
        TextView trackTextView = (TextView)view.findViewById(R.id.event_detail_track);
        trackTextView.setVisibility(track != null && !track.isEmpty() ? View.VISIBLE : View.GONE);
        trackTextView.setText(track);
    }

    @Override
    public void setDescription(String description) {
        TextView descriptionTextView = (TextView)view.findViewById(R.id.event_detail_description);
        descriptionTextView.setVisibility(description != null && !description.isEmpty() ? View.VISIBLE : View.GONE);
        descriptionTextView.setText(description != null && !description.isEmpty() ? Html.fromHtml(description) : "");
        descriptionTextView.setMovementMethod(new CustomLinkMovementMethod());
    }

    @Override
    public void setDate(String date) {
        TextView dateTextView = (TextView)view.findViewById(R.id.event_detail_date);
        ((LinearLayout)dateTextView.getParent()).setVisibility(date != null && !date.isEmpty() ? View.VISIBLE : View.GONE);
        dateTextView.setText(date);
    }

    @Override
    public void setLocation(String location) {
        TextView locationTextView = (TextView)view.findViewById(R.id.event_detail_place);
        ((LinearLayout)locationTextView.getParent()).setVisibility(location != null && !location.isEmpty() ? View.VISIBLE : View.GONE);
        locationTextView.setText(location);
    }

    @Override
    public void setCredentials(String credentials) {
        TextView credentialsTextView = (TextView)view.findViewById(R.id.event_detail_credentials);
        ((LinearLayout)credentialsTextView.getParent()).setVisibility(credentials != null && !credentials.isEmpty() ? View.VISIBLE : View.GONE);
        credentialsTextView.setText(credentials);
    }

    @Override
    public void setLevel(String level) {
        TextView levelTextView = (TextView)view.findViewById(R.id.event_detail_level);
        ((LinearLayout)levelTextView.getParent()).setVisibility(level != null && !level.isEmpty() ? View.VISIBLE : View.GONE);
        levelTextView.setText(level);
    }

    @Override
    public void setSponsors(String sponsors) {
        TextView sponsorsTextView = (TextView)view.findViewById(R.id.event_detail_sponsors);
        sponsorsTextView.setVisibility(sponsors != null && !sponsors.isEmpty() ? View.VISIBLE : View.GONE);
        sponsorsTextView.setText(sponsors);
    }

    @Override
    public void setTags(String tags) {
        TextView tagsTextView = (TextView)view.findViewById(R.id.event_detail_tags);
        ((LinearLayout)tagsTextView.getParent()).setVisibility(tags != null && !tags.isEmpty() ? View.VISIBLE : View.GONE);
        tagsTextView.setText(tags);
    }

    @Override
    public void setSpeakers(List<PersonListItemDTO> speakers) {
        speakerListAdapter.clear();
        speakerListAdapter.addAll(speakers);

        LinearListView speakerList = (LinearListView)view.findViewById(R.id.event_detail_speakers_list);
        speakerList.setVisibility(speakers.size() > 0 ? View.VISIBLE : View.GONE);
        speakerList.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                presenter.showSpeakerProfile(position);
            }
        });
    }

    @Override
    public void setIsScheduledStatusVisible(Boolean isScheduledStatusVisible) {
        if (menu != null && menu.findItem(R.id.action_scheduled_status) != null) {
            setIsScheduledStatusVisibleInternal(isScheduledStatusVisible);
        }
        else {
            this.isScheduledStatusVisible = isScheduledStatusVisible;
        }
    }

    @Override
    public void setAllowNewFeedback(boolean allowNewFeedback) {
        if (menu != null && menu.findItem(R.id.action_scheduled_status) != null) {
            setIAllowNewFeedbackInternal(allowNewFeedback);
        }
        else {
            this.allowNewFeedback = allowNewFeedback;
        }
    }

    @Override
    public void setAllowRsvp(boolean allowRsvp) {
        if (menu != null && menu.findItem(R.id.action_rsvp) != null) {
            setScheduledInternal(allowRsvp);
        }
        else {
            this.allowRsvp = allowRsvp;
        }
    }

    private void setIAllowNewFeedbackInternal(boolean allowNewFeedback) {
        menu.findItem(R.id.action_create_feedback).setVisible(allowNewFeedback);
        this.allowNewFeedback = allowNewFeedback;
    }

    private void setIAllowRsvpInternal(boolean allowRsvp) {
        menu.findItem(R.id.action_rsvp).setVisible(allowRsvp);
    }

    @Override
    public void setScheduled(Boolean scheduled) {
        if (menu != null && menu.findItem(R.id.action_scheduled_status) != null) {
            setScheduledInternal(scheduled);
        }
        else {
            this.scheduled = scheduled;
        }
    }

    @Override
    public Boolean getScheduled() {
        return this.scheduled;
    }

    private void setScheduledInternal(Boolean scheduled) {
        MenuItem scheduledMenuItem = menu.findItem(R.id.action_scheduled_status);
        scheduledMenuItem.setIcon(scheduled ? R.drawable.checked_active : R.drawable.unchecked);
        this.scheduled = scheduled;
    }

    public void setIsScheduledStatusVisibleInternal(Boolean isScheduledStatusVisible) {
        menu.findItem(R.id.action_scheduled_status).setVisible(isScheduledStatusVisible);
        this.isScheduledStatusVisible = isScheduledStatusVisible;
    }

    @Override
    public void setAverageRate(int rate) {
        LinearLayout averageFeedbackLayout = (LinearLayout)view.findViewById(R.id.event_avg_feedback_rate);

        if (rate == 0) {
            averageFeedbackLayout.setVisibility(View.GONE);
            return;
        }

        averageFeedbackLayout.setVisibility(View.VISIBLE);
        ImageView rate1 = (ImageView)view.findViewById(R.id.event_avg_feedback_rate_1);
        ImageView rate2 = (ImageView)view.findViewById(R.id.event_avg_feedback_rate_2);
        ImageView rate3 = (ImageView)view.findViewById(R.id.event_avg_feedback_rate_3);
        ImageView rate4 = (ImageView)view.findViewById(R.id.event_avg_feedback_rate_4);
        ImageView rate5 = (ImageView)view.findViewById(R.id.event_avg_feedback_rate_5);

        if (rate >= 1) {
            rate1.setImageResource(R.drawable.star_color);
        }
        if (rate >= 2) {
            rate2.setImageResource(R.drawable.star_color);
        }
        if (rate >= 3) {
            rate3.setImageResource(R.drawable.star_color);
        }
        if (rate >= 4) {
            rate4.setImageResource(R.drawable.star_color);
        }
        if (rate >= 5) {
            rate5.setImageResource(R.drawable.star_color);
        }
    }

    @Override
    public void setMyFeedbackRate(int rate) {
        ImageView rate1 = (ImageView)view.findViewById(R.id.item_feedback_rate_1);
        ImageView rate2 = (ImageView)view.findViewById(R.id.item_feedback_rate_2);
        ImageView rate3 = (ImageView)view.findViewById(R.id.item_feedback_rate_3);
        ImageView rate4 = (ImageView)view.findViewById(R.id.item_feedback_rate_4);
        ImageView rate5 = (ImageView)view.findViewById(R.id.item_feedback_rate_5);

        if (rate >= 1) {
            rate1.setImageResource(R.drawable.star_color);
        }
        if (rate >= 2) {
            rate2.setImageResource(R.drawable.star_color);
        }
        if (rate >= 3) {
            rate3.setImageResource(R.drawable.star_color);
        }
        if (rate >= 4) {
            rate4.setImageResource(R.drawable.star_color);
        }
        if (rate >= 5) {
            rate5.setImageResource(R.drawable.star_color);
        }
    }

    @Override
    public void setMyFeedbackReview(String review) {
        TextView reviewText = (TextView)view.findViewById(R.id.item_feedback_review);
        reviewText.setText(review);
    }

    @Override
    public void setMyFeedbackDate(String date) {
        TextView dateText = (TextView)view.findViewById(R.id.item_feedback_date);
        dateText.setText(date);
    }

    @Override
    public void setMyFeedbackOwner(String owner) {
        TextView ownerText = (TextView)view.findViewById(R.id.item_feedback_owner);
        ownerText.setText(owner);
    }

    @Override
    public void hasMyFeedback(boolean hasMyFeedback) {
        LinearLayout myFeedbackLayout = (LinearLayout)view.findViewById(R.id.event_detail_my_feedback);
        myFeedbackLayout.setVisibility(hasMyFeedback ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showFeedbackActivityIndicator() {
        LinearLayout loadingIndicatorLayout = (LinearLayout)view.findViewById(R.id.event_detail_loading_feedback);
        loadingIndicatorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFeedbackContainer() {
        LinearLayout feedbackContainerLayout = (LinearLayout)view.findViewById(R.id.event_detail_feedback_container);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(2000);

        feedbackContainerLayout.setVisibility(View.VISIBLE);
        feedbackContainerLayout.setAnimation(fadeIn);
    }

    @Override
    public void hideFeedbackActivityIndicator() {
        LinearLayout loadingIndicatorLayout = (LinearLayout)view.findViewById(R.id.event_detail_loading_feedback);
        loadingIndicatorLayout.setVisibility(View.GONE);
    }

    @Override
    public void setOtherPeopleFeedback(List<FeedbackDTO> feedback) {
        feedbackListAdapter.clear();
        feedbackListAdapter.addAll(feedback);
        feedbackListAdapter.notifyDataSetChanged();

        LinearListView feedbackList = (LinearListView)view.findViewById(R.id.event_detail_feedback_list);
        feedbackList.setVisibility(feedback.size() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void toggleLoadMore(boolean show) {
        Button loadMoreButton = (Button)view.findViewById(R.id.event_detail_load_more_feedback_button);
        loadMoreButton.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showFeedbackErrorMessage(String message) {
        TextView feedbackErrorTextView = (TextView)view.findViewById(R.id.event_detail_error_loading_feedback_text);
        feedbackErrorTextView.setText(message);
        ((LinearLayout)feedbackErrorTextView.getParent()).setVisibility(View.VISIBLE);
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
        };
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
        };
    }

    private class CustomLinkMovementMethod extends LinkMovementMethod {

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            try {
                return super.onTouchEvent(widget, buffer, event);
            } catch (Exception ex) {
                showInfoMessage("Could not navigate this link");
                return true;
            }
        }
    }
}
