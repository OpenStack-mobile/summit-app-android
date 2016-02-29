package org.openstack.android.summit.modules.venue_list.user_interface;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.linearlistview.LinearListView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.VenueDTO;
import org.openstack.android.summit.common.user_interface.BaseFragment;

import java.util.List;

/**
 * Created by Claudio Redi on 2/11/2016.
 */
public class VenueListFragment extends BaseFragment<IVenueListPresenter> implements IVenueListView  {

    private InternalVenueListAdapter internalVenueListAdapter;
    private ExternalVenueListAdapter externalVenueListAdapter;

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
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venue_list, container, false);
        this.view = view;

        LinearListView internalVenueList = (LinearListView)view.findViewById(R.id.venues_internal_list);
        internalVenueList.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                presenter.showInternalVenueDetail(position);
            }
        });
        internalVenueListAdapter = new InternalVenueListAdapter(getContext());
        internalVenueList.setAdapter(internalVenueListAdapter);

        LinearListView externalVenueList = (LinearListView)view.findViewById(R.id.venues_external_list);
        externalVenueList.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                presenter.showExternalVenueDetail(position);
            }
        });
        externalVenueListAdapter = new ExternalVenueListAdapter(getContext());
        externalVenueList.setAdapter(externalVenueListAdapter);

        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void setInternalVenues(List<VenueDTO> venues) {
        internalVenueListAdapter.clear();
        internalVenueListAdapter.addAll(venues);
    }

    @Override
    public void setExternalVenues(List<VenueDTO> venues) {
        externalVenueListAdapter.clear();
        externalVenueListAdapter.addAll(venues);

        LinearLayout externalSubsectionHeader = (LinearLayout)view.findViewById(R.id.venue_list_external_venues_subsection_header);
        externalSubsectionHeader.setVisibility(venues.size() > 0 ? View.VISIBLE : View.GONE);
    }

    private class InternalVenueListAdapter extends ArrayAdapter<VenueDTO> {

        public InternalVenueListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_internal_venue_list, parent, false);
            }

            final VenueListItemView venueListItemView = new VenueListItemView(convertView);

            presenter.buildInternalVenueItem(venueListItemView, position);

            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        };
    }

    private class ExternalVenueListAdapter extends ArrayAdapter<VenueDTO> {

        public ExternalVenueListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_external_venue_list, parent, false);
            }

            final VenueListItemView venueListItemView = new VenueListItemView(convertView);

            presenter.buildExternalVenueItem(venueListItemView, position);

            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        };
    }
}