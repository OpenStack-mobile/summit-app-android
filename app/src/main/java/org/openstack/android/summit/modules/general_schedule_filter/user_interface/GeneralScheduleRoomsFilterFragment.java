package org.openstack.android.summit.modules.general_schedule_filter.user_interface;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.linearlistview.LinearListView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.user_interface.BaseFragment;

import java.util.List;

public class GeneralScheduleRoomsFilterFragment
        extends BaseFragment<IGeneralScheduleRoomsFilterPresenter>
        implements IGeneralScheduleRoomsFilterView
{

    private Integer venueId = null;

    public GeneralScheduleRoomsFilterFragment(){
        super();
    }

    private RoomsListAdapter roomsListAdapter;

    public static GeneralScheduleRoomsFilterFragment newInstance(int venueId){
        final GeneralScheduleRoomsFilterFragment fragment = new GeneralScheduleRoomsFilterFragment();
        final Bundle args = new Bundle();
        args.putInt("VenueId", venueId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_general_schedule_room_filter, container, false);
        final Bundle args = getArguments();
        if(args != null){
            venueId = args.getInt("VenueId", 0);
        }
        LinearListView roomsList = (LinearListView) view.findViewById(R.id.filter_rooms_list);
        roomsListAdapter = new GeneralScheduleRoomsFilterFragment.RoomsListAdapter(getContext());
        roomsList.setAdapter(roomsListAdapter);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void showRooms(List<NamedDTO> rooms) {
        roomsListAdapter.clear();
        roomsListAdapter.addAll(rooms);
    }

    @Override
    public Integer getSelectedVenueId() {
        return this.venueId;
    }

    private class RoomsListAdapter extends ArrayAdapter<NamedDTO> {

        public RoomsListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_filter_list, parent, false);
            }

            GeneralScheduleFilterItemView generalScheduleFilterItemView = new GeneralScheduleFilterItemView(convertView);
            presenter.buildRoomFilterItem(generalScheduleFilterItemView, position);
            generalScheduleFilterItemView.setItemCallback(isChecked -> {
                presenter.toggleSelectionRooms(generalScheduleFilterItemView, position);
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
