package org.openstack.android.summit.modules.event_detail.user_interface;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linearlistview.LinearListView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.PersonItemView;

import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailFragment extends BaseFragment implements IEventDetailView {

    @Inject
    IEventDetailPresenter presenter;
    private SpeakerListAdapter speakerListAdapter;
    private View view;
    private Menu menu;
    private Boolean scheduled;
    private Boolean isScheduledStatusVisible;

    public EventDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        presenter.setView(this);
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
        LinearListView speakerList = (LinearListView)view.findViewById(R.id.event_detail_list_speakers);
        speakerListAdapter = new SpeakerListAdapter(getContext());
        speakerList.setAdapter(speakerListAdapter);

        presenter.onCreate(savedInstanceState);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.event_detail, menu);
        this.menu = menu;
        setScheduledInternal(scheduled);
        setIsScheduledStatusVisibleInternal(isScheduledStatusVisible);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_scheduled_status) {
            presenter.toggleScheduleStatus();
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
        speakerListAdapter.notifyDataSetChanged();

        LinearListView speakerList = (LinearListView)view.findViewById(R.id.event_detail_list_speakers);
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
}
