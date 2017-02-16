package org.openstack.android.summit.modules.search.user_interface;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.linearlistview.LinearListView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.IPersonItemView;
import org.openstack.android.summit.common.user_interface.ISimpleListItemView;
import org.openstack.android.summit.common.user_interface.PersonItemView;
import org.openstack.android.summit.common.user_interface.SimpleListItemView;
import org.openstack.android.summit.common.user_interface.recycler_view.DividerItemDecoration;
import org.openstack.android.summit.common.user_interface.schedule_list.ScheduleListAdapter;

import java.util.List;

import javax.inject.Inject;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

/**
 * Created by Claudio Redi on 1/14/2016.
 */
public class SearchFragment extends BaseFragment<ISearchPresenter> implements ISearchView {

    private TrackListAdapter trackListAdapter;
    private SpeakerListAdapter speakerListAdapter;
    private ScheduleListAdapter scheduleListAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_result, container, false);

        EditText searchText = (EditText)view.findViewById(R.id.search_results_edittext);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String searchTerm = v.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_DONE && !searchTerm.isEmpty()) {
                    presenter.search(searchTerm);
                }
                return false;
            }
        });

        RecyclerView eventsList = (RecyclerView)view.findViewById(R.id.search_results_events_list);
        scheduleListAdapter     = new ScheduleListAdapter(presenter);
        layoutManager           = new LinearLayoutManager(getContext());

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        eventsList.setLayoutManager(layoutManager);
        eventsList.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);

        eventsList.addItemDecoration(itemDecoration);

        eventsList.setAdapter(scheduleListAdapter);

        LinearListView trackList = (LinearListView)view.findViewById(R.id.search_results_tracks_list);
        trackListAdapter = new TrackListAdapter(getContext());
        trackList.setAdapter(trackListAdapter);
        trackList.setOnItemClickListener((parent, view12, position, id) -> presenter.showTrackSchedule(position));

        LinearListView speakersList = (LinearListView)view.findViewById(R.id.search_results_speakers_list);
        speakerListAdapter = new SpeakerListAdapter(getContext());
        speakersList.setAdapter(speakerListAdapter);
        speakersList.setOnItemClickListener((parent, view1, position, id) -> presenter.showSpeakerProfile(position));

        subsectionLinksSetup();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(getResources().getString(R.string.search));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        presenter.onSaveInstanceState(outState);
    }

    private void subsectionLinksSetup() {
        Button eventsButton = (Button)view.findViewById(R.id.search_results_events_subsection_button);
        eventsButton.setOnClickListener(v -> {
            LinearLayout eventsHeader = (LinearLayout)view.findViewById(R.id.search_results_events_subsection_header);
            ScrollView scrollView = (ScrollView)view.findViewById(R.id.search_results_container_scroll);
            scrollView.smoothScrollTo(0, eventsHeader.getTop());
        });

        Button tracksButton = (Button)view.findViewById(R.id.search_results_tracks_subsection_button);
        tracksButton.setOnClickListener(v -> {
            LinearLayout tracksHeader = (LinearLayout)view.findViewById(R.id.search_results_tracks_subsection_header);
            ScrollView scrollView = (ScrollView)view.findViewById(R.id.search_results_container_scroll);
            scrollView.smoothScrollTo(0, tracksHeader.getTop());
        });

        Button speakersButton = (Button)view.findViewById(R.id.search_results_speakers_subsection_button);
        speakersButton.setOnClickListener(v -> {
            LinearLayout speakersHeader = (LinearLayout) view.findViewById(R.id.search_results_speakers_subsection_header);
            ScrollView scrollView = (ScrollView) view.findViewById(R.id.search_results_container_scroll);
            scrollView.smoothScrollTo(0, speakersHeader.getTop());
        });
    }

    @Override
    public void showEvents(List<ScheduleItemDTO> events) {
        scheduleListAdapter.clear();
        scheduleListAdapter.addAll(events);

        TextView noResultsEventsText = (TextView)view.findViewById(R.id.search_results_empty_message_events_text);
        noResultsEventsText.setVisibility(View.GONE);

        Button eventsButton = (Button)view.findViewById(R.id.search_results_events_subsection_button);
        eventsButton.setText(String.format("%s (%d)", getResources().getString(R.string.events), events.size()));

        RecyclerView eventsList = (RecyclerView)view.findViewById(R.id.search_results_events_list);
        eventsList.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTracks(List<NamedDTO> tracks) {
        trackListAdapter.clear();
        trackListAdapter.addAll(tracks);

        TextView noResultsEventsText = (TextView)view.findViewById(R.id.search_results_empty_message_tracks_text);
        noResultsEventsText.setVisibility(View.GONE);

        Button tracksButton = (Button)view.findViewById(R.id.search_results_tracks_subsection_button);
        tracksButton.setText(String.format("%s (%d)", getResources().getString(R.string.tracks), tracks.size()));

        LinearListView tracksList = (LinearListView)view.findViewById(R.id.search_results_tracks_list);
        tracksList.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSpeakers(List<PersonListItemDTO> speakers) {
        speakerListAdapter.clear();
        speakerListAdapter.addAll(speakers);

        TextView noResultsEventsText = (TextView)view.findViewById(R.id.search_results_empty_message_speakers_text);
        noResultsEventsText.setVisibility(View.GONE);

        Button speakersButton = (Button)view.findViewById(R.id.search_results_speakers_subsection_button);
        speakersButton.setText(String.format("%s (%d)", getResources().getString(R.string.speakers), speakers.size()));

        LinearListView speakersList = (LinearListView)view.findViewById(R.id.search_results_speakers_list);
        speakersList.setVisibility(View.VISIBLE);
    }

    @Override
    public void setSearchTerm(String searchTerm) {
        EditText searchText = (EditText)view.findViewById(R.id.search_results_edittext);
        searchText.setText(searchTerm);
    }

    @Override
    public void showNoResultsForEvents() {
        TextView noResultsText = (TextView)view.findViewById(R.id.search_results_empty_message_events_text);
        noResultsText.setVisibility(View.VISIBLE);

        Button eventsButton = (Button)view.findViewById(R.id.search_results_events_subsection_button);
        eventsButton.setText(String.format("%s (0)", getResources().getString(R.string.events)));

        RecyclerView eventsList = (RecyclerView)view.findViewById(R.id.search_results_events_list);
        eventsList.setVisibility(View.GONE);
    }

    @Override
    public void showNoResultsForTracks() {
        TextView noResultsText = (TextView)view.findViewById(R.id.search_results_empty_message_tracks_text);
        noResultsText.setVisibility(View.VISIBLE);

        Button tracksButton = (Button)view.findViewById(R.id.search_results_tracks_subsection_button);
        tracksButton.setText(String.format("%s (0)", getResources().getString(R.string.tracks)));

        LinearListView tracksList = (LinearListView)view.findViewById(R.id.search_results_tracks_list);
        tracksList.setVisibility(View.GONE);
    }

    @Override
    public void showNoResultsForSpeakers() {
        TextView noResultsText = (TextView)view.findViewById(R.id.search_results_empty_message_speakers_text);
        noResultsText.setVisibility(View.VISIBLE);

        Button speakersButton = (Button)view.findViewById(R.id.search_results_speakers_subsection_button);
        speakersButton.setText(String.format("%s (0)", getResources().getString(R.string.speakers)));

        LinearListView speakersList = (LinearListView)view.findViewById(R.id.search_results_speakers_list);
        speakersList.setVisibility(View.GONE);
    }

    @Override
    public void showActivityIndicator() {
        LinearLayout saerchResultContainer = (LinearLayout)view.findViewById(R.id.search_results_container);
        saerchResultContainer.setVisibility(View.GONE);

        LinearLayout subsectionBar = (LinearLayout)view.findViewById(R.id.search_results_subsection_bar);
        subsectionBar.setVisibility(View.GONE);

        super.showActivityIndicator(0);
    }

    @Override
    public void hideActivityIndicator() {
        if(view != null) {
            LinearLayout saerchResultContainer = (LinearLayout) view.findViewById(R.id.search_results_container);
            saerchResultContainer.setVisibility(View.VISIBLE);

            LinearLayout subsectionBar = (LinearLayout) view.findViewById(R.id.search_results_subsection_bar);
            subsectionBar.setVisibility(View.VISIBLE);
        }
        super.hideActivityIndicator();
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

            final IPersonItemView personItemView = new PersonItemView(convertView);

            presenter.buildSpeakerItem(personItemView, position);

            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        };
    }

    private class TrackListAdapter extends ArrayAdapter<NamedDTO> {

        public TrackListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_simple_list, parent, false);
            }

            final ISimpleListItemView simpleListItemView = new SimpleListItemView(convertView);

            presenter.buildTrackItem(simpleListItemView, position);

            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        };
    }
}
