package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import com.linearlistview.LinearListView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.TrackGroupDTO;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.ISimpleListItemView;
import org.openstack.android.summit.common.user_interface.SimpleListItemView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 2/1/2016.
 */
public class GeneralScheduleFilterFragment extends BaseFragment implements IGeneralScheduleFilterView {
    @Inject
    IGeneralScheduleFilterPresenter presenter;

    private SummitTypeListAdapter summitTypeListAdapter;
    private TrackGroupListAdapter trackGroupListAdapter;
    private EventTypeListAdapter eventTypeListAdapter;
    private LevelListAdapter levelListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);

        presenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_general_schedule_filter, container, false);

        LinearListView summitTypesList = (LinearListView) view.findViewById(R.id.filter_summit_types_list);
        summitTypeListAdapter = new SummitTypeListAdapter(getContext());
        summitTypesList.setAdapter(summitTypeListAdapter);
        summitTypesList.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                GeneralScheduleFilterItemView generalScheduleFilterItemView = new GeneralScheduleFilterItemView(view);
                presenter.toggleSelectionSummitType(generalScheduleFilterItemView, position);
            }
        });

        LinearListView trackGroupList = (LinearListView) view.findViewById(R.id.filter_track_groups_list);
        trackGroupListAdapter = new TrackGroupListAdapter(getContext());
        trackGroupList.setAdapter(trackGroupListAdapter);
        trackGroupList.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                GeneralScheduleFilterItemView generalScheduleFilterItemView = new GeneralScheduleFilterItemView(view);
                presenter.toggleSelectionTrackGroup(generalScheduleFilterItemView, position);
            }
        });

        LinearListView eventTypesList = (LinearListView) view.findViewById(R.id.filter_event_types_list);
        eventTypeListAdapter = new EventTypeListAdapter(getContext());
        eventTypesList.setAdapter(eventTypeListAdapter);
        eventTypesList.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                GeneralScheduleFilterItemView generalScheduleFilterItemView = new GeneralScheduleFilterItemView(view);
                presenter.toggleSelectionEventType(generalScheduleFilterItemView, position);
            }
        });

        LinearListView levelList = (LinearListView) view.findViewById(R.id.filter_levels_list);
        levelListAdapter = new LevelListAdapter(getContext());
        levelList.setAdapter(levelListAdapter);
        levelList.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                GeneralScheduleFilterItemView generalScheduleFilterItemView = new GeneralScheduleFilterItemView(view);
                presenter.toggleSelectionLevel(generalScheduleFilterItemView, position);
            }
        });

        presenter.onCreate(savedInstanceState);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
        setTitle(getResources().getString(R.string.filter));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        presenter.onSaveInstanceState(outState);
    }

    @Override
    public void showSummitTypes(List<NamedDTO> summitTypes) {
        summitTypeListAdapter.clear();
        summitTypeListAdapter.addAll(summitTypes);
    }

    @Override
    public void showTrackGroups(List<TrackGroupDTO> trackGroups) {
        trackGroupListAdapter.clear();
        trackGroupListAdapter.addAll(trackGroups);
    }

    @Override
    public void showEventTypes(List<NamedDTO> eventTypes) {
        eventTypeListAdapter.clear();
        eventTypeListAdapter.addAll(eventTypes);
    }

    @Override
    public void showLevels(List<String> levels) {
        levelListAdapter.clear();
        levelListAdapter.addAll(levels);
    }

    private class SummitTypeListAdapter extends ArrayAdapter<NamedDTO> {

        public SummitTypeListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_filter_list, parent, false);
            }

            GeneralScheduleFilterItemView generalScheduleFilterItemView = new GeneralScheduleFilterItemView(convertView);
            presenter.buildSummitTypeFilterItem(generalScheduleFilterItemView, position);

            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }
    }

    private class EventTypeListAdapter extends ArrayAdapter<NamedDTO> {

        public EventTypeListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_filter_list, parent, false);
            }

            GeneralScheduleFilterItemView generalScheduleFilterItemView = new GeneralScheduleFilterItemView(convertView);
            presenter.buildEventTypeFilterItem(generalScheduleFilterItemView, position);

            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }
    }

    private class TrackGroupListAdapter extends ArrayAdapter<TrackGroupDTO> {

        public TrackGroupListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_filter_list, parent, false);
            }

            GeneralScheduleFilterItemView generalScheduleFilterItemView = new GeneralScheduleFilterItemView(convertView);
            presenter.buildTrackGroupFilterItem(generalScheduleFilterItemView, position);

            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }
    }

    private class LevelListAdapter extends ArrayAdapter<String> {

        public LevelListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_filter_list, parent, false);
            }

            GeneralScheduleFilterItemView generalScheduleFilterItemView = new GeneralScheduleFilterItemView(convertView);
            presenter.buildLevelFilterItem(generalScheduleFilterItemView, position);

            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }
    }
}