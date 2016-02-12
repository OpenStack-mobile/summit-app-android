package org.openstack.android.summit.modules.track_list.user_interface;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.TrackDTO;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.SimpleListItemView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrackListFragment extends BaseFragment<ITrackListPresenter> implements ITrackListView {

    private TrackListAdapter trackListAdapter;

    public TrackListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        setTitle(getResources().getString(R.string.events));
        super.onResume();
    }

    // HACK: the tab container preload this tab on initial load, at that time data is not yet on local database so without this
    // list show up empty on first load
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (isVisible()) {
            if (isVisibleToUser) {
                if (presenter != null) {
                    presenter.reloadData();
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_level_list, container, false);
        this.view = view;
        ListView trackList = (ListView)view.findViewById(R.id.list_levels);
        trackListAdapter = new TrackListAdapter(getContext());
        trackList.setAdapter(trackListAdapter);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    public void setTracks(List<TrackDTO> tracks) {
        trackListAdapter.clear();
        trackListAdapter.addAll(tracks);
    }

    private class TrackListAdapter extends ArrayAdapter<TrackDTO> {

        public TrackListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_simple_list, parent, false);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.showTrackEvents(position);
                }
            });

            final SimpleListItemView trackListItemView = new SimpleListItemView(convertView);

            presenter.buildItem(trackListItemView, position);

            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        };
    }
}
