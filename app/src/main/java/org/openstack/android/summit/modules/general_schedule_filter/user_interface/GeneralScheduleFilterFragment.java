package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.linearlistview.LinearListView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.TrackGroupDTO;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.ISimpleListItemView;
import org.openstack.android.summit.common.user_interface.SimpleListItemView;

import java.util.List;

import javax.inject.Inject;

import me.kaede.tagview.OnTagDeleteListener;
import me.kaede.tagview.Tag;
import me.kaede.tagview.TagView;

/**
 * Created by Claudio Redi on 2/1/2016.
 */
public class GeneralScheduleFilterFragment extends BaseFragment<IGeneralScheduleFilterPresenter> implements IGeneralScheduleFilterView {

    private TrackGroupListAdapter trackGroupListAdapter;
    private EventTypeListAdapter  eventTypeListAdapter;
    private LevelListAdapter      levelListAdapter;
    private ArrayAdapter<String>  tagsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_general_schedule_filter, container, false);

        final CheckBox hidePastTalks = (CheckBox) view.findViewById(R.id.hide_past_talks);
        hidePastTalks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.toggleHidePastTalks(hidePastTalks.isChecked());
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

        AutoCompleteTextView tagsTextView = (AutoCompleteTextView)view.findViewById(R.id.filter_tags_autocomplete);
        tagsTextView.setThreshold(2);
        tagsTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tagText = (String)parent.getItemAtPosition(position);
                presenter.addTag(tagText);
            }
        });

        TagView tagView = (TagView)view.findViewById(R.id.filter_tags_list);
        tagView.setOnTagDeleteListener(new OnTagDeleteListener() {
            @Override
            public void onTagDeleted(Tag tag, int i) {
                presenter.removeTag(tag.text);
            }
        });

        super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }

    public void addTag(String tagText) {
        AutoCompleteTextView tagsTextView = (AutoCompleteTextView)view.findViewById(R.id.filter_tags_autocomplete);
        TagView tagView                   = (TagView)view.findViewById(R.id.filter_tags_list);
        Tag tag                           = new Tag(tagText);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tag.layoutBorderColor = view.getResources().getColor(R.color.openStackGray, null);
        }
        else {
            tag.layoutBorderColor = view.getResources().getColor(R.color.openStackGray);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tag.layoutColor = view.getResources().getColor(R.color.openStackLightBlue, null);
        }
        else {
            tag.layoutColor = view.getResources().getColor(R.color.openStackLightBlue);
        }

        tag.layoutBorderSize = 1;
        tag.isDeletable = true;
        tagView.addTag(tag);

        tagsTextView.setText("");
    }

    @Override
    public void toggleShowPastTalks(boolean isChecked) {
        CheckBox hidePastTalks = (CheckBox) view.findViewById(R.id.hide_past_talks);
        hidePastTalks.setChecked(isChecked);
    }

    @Override
    public void showShowPastTalks(boolean show) {
        LinearLayout header    = (LinearLayout) view.findViewById(R.id.hide_past_talks_header);
        LinearLayout container = (LinearLayout) view.findViewById(R.id.hide_past_talks_container);
        header.setVisibility(show ? View.VISIBLE : View.GONE);
        container.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
        setTitle(getResources().getString(R.string.filter));
    }

    @Override
    public void onPause() {
        super.onPause();
        hideKeyboard(getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        presenter.onSaveInstanceState(outState);
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

    @Override
    public void bindTags(List<String> tags) {
        AutoCompleteTextView tagsTextView = (AutoCompleteTextView)view.findViewById(R.id.filter_tags_autocomplete);
        tagsAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1, tags);
        tagsTextView.setAdapter(tagsAdapter);
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