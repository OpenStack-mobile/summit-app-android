package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.linearlistview.LinearListView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.TrackDTO;
import org.openstack.android.summit.common.DTOs.TrackGroupDTO;
import org.openstack.android.summit.common.user_interface.BaseFragment;

import java.util.List;

/**
 * Created by smarcet on 2/2/18.
 */

public class GeneralScheduleTracksFilterFragment
    extends BaseFragment<IGeneralScheduleTracksFilterPresenter>
    implements IGeneralScheduleTracksFilterView

{
    private Integer trackGroupId = null;

    public GeneralScheduleTracksFilterFragment(){
        super();
    }

    public static GeneralScheduleTracksFilterFragment newInstance(int trackGroupId){
        final GeneralScheduleTracksFilterFragment fragment = new GeneralScheduleTracksFilterFragment();
        final Bundle args = new Bundle();
        args.putInt("TrackGroupId", trackGroupId);
        fragment.setArguments(args);
        return fragment;
    }

    private TrackListAdapter trackListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_general_schedule_track_filter, container, false);
        final Bundle args = getArguments();
        if(args != null){
            trackGroupId = args.getInt("TrackGroupId", 0);
        }
        LinearListView trackList = (LinearListView) view.findViewById(R.id.filter_track_list);
        trackListAdapter = new GeneralScheduleTracksFilterFragment.TrackListAdapter(getContext());
        trackList.setAdapter(trackListAdapter);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void showTracks(List<TrackDTO> tracks) {
        trackListAdapter.clear();
        trackListAdapter.addAll(tracks);
    }

    @Override
    public Integer getSelectedTrackGroupId() {
        return this.trackGroupId;
    }

    private class TrackListAdapter extends ArrayAdapter<TrackDTO> {

        public TrackListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_filter_list, parent, false);
            }

            GeneralScheduleFilterItemView generalScheduleFilterItemView = new GeneralScheduleFilterItemView(convertView);
            presenter.buildTrackFilterItem(generalScheduleFilterItemView, position);
            generalScheduleFilterItemView.setItemCallback(isChecked -> {
                presenter.toggleSelectionTrack(generalScheduleFilterItemView, position);
            });
            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }
    }
}
