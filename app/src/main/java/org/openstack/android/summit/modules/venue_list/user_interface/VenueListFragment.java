package org.openstack.android.summit.modules.venue_list.user_interface;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.TrackDTO;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.SimpleListItemView;

import java.util.List;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenueListFragment extends BaseFragment<IVenueListPresenter> implements IVenueListView  {

    private VenueListAdapter venueListAdapter;

    public VenueListFragment() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_level_list, container, false);
        this.view = view;
        ListView venueList = (ListView)view.findViewById(R.id.list_levels);
        venueListAdapter = new VenueListAdapter(getContext());
        venueList.setAdapter(venueListAdapter);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void setVenues(List<NamedDTO> venues) {
        venueListAdapter.clear();
        venueListAdapter.addAll(venues);
    }

    private class VenueListAdapter extends ArrayAdapter<NamedDTO> {

        public VenueListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_venue_list, parent, false);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.showVenueDetail(position);
                }
            });

            final VenueListItemView venueListItemView = new VenueListItemView(convertView);

            presenter.buildItem(venueListItemView, position);

            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        };
    }
}