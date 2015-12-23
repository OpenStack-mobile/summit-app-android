package org.openstack.android.summit.modules.general_schedule.user_interface;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.ScheduleItem;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeneralScheduleFragment extends BaseFragment {

    @Inject
    IGeneralSchedulePresenter presenter;
    private GeneralScheduleListAdapter generalScheduleListAdapter;
    private List<ScheduleItemDTO> events;
    private Date startDate;
    private Date endDate;

    public GeneralScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        presenter.setView(this);
        presenter.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general_schedule, container, false);
        ListView scheduleList = (ListView)view.findViewById(R.id.list_schedule);
        generalScheduleListAdapter = new GeneralScheduleListAdapter(getContext());
        scheduleList.setAdapter(generalScheduleListAdapter);

        return view;
    }

    public List<ScheduleItemDTO> getEvents() {
        return events;
    }

    public void setEvents(List<ScheduleItemDTO> events) {
        this.events = events;
        generalScheduleListAdapter.addAll(events);
        generalScheduleListAdapter.notifyDataSetChanged();
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    private class GeneralScheduleListAdapter extends ArrayAdapter<ScheduleItemDTO> {

        public GeneralScheduleListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_schedule, parent, false);
            }

            ScheduleItem scheduleItem = new ScheduleItem(convertView);
            presenter.buildItem(scheduleItem, position);

            // Return the completed view to render on screen
            return convertView;
        }
    }
}
