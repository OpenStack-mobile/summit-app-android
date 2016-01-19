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
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.SimpleListItemView;

import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrackListFragment extends BaseFragment implements ITrackListFragment {

    @Inject
    ITrackListPresenter presenter;

    private TrackListAdapter trackListAdapter;

    public TrackListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        presenter.setView(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        getActivity().setTitle("EVENTS");
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_level_list, container, false);
        this.view = view;
        ListView trackList = (ListView)view.findViewById(R.id.list_levels);
        trackListAdapter = new TrackListAdapter(getContext());
        trackList.setAdapter(trackListAdapter);
        presenter.onCreate(savedInstanceState);
        return view;
    }

    @Override
    public void reloadData() {
        trackListAdapter.notifyDataSetChanged();
    }

    public void setTracks(List<NamedDTO> tracks) {
        trackListAdapter.clear();
        trackListAdapter.addAll(tracks);
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
