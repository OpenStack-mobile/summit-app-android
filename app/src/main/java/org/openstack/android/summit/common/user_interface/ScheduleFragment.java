package org.openstack.android.summit.common.user_interface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.andressantibanez.ranger.Ranger;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 12/29/2015.
 */
public class ScheduleFragment<P extends ISchedulePresenter> extends BaseFragment<P> implements IScheduleView {

    ScheduleListAdapter scheduleListAdapter;
    List<ScheduleItemDTO> events;
    Parcelable listState = null;
    private static final String LIST_STATE = "scheduleListState";

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setView(this);
        if(savedInstanceState != null) {
            listState = savedInstanceState.getParcelable(LIST_STATE);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView scheduleList = (ListView)view.findViewById(R.id.list_schedule);
        scheduleListAdapter = new ScheduleListAdapter(getContext());
        scheduleList.setAdapter(scheduleListAdapter);

        Ranger ranger = (Ranger) view.findViewById(R.id.ranger_summit);
        ranger.setDayViewOnClickListener(new Ranger.DayViewOnClickListener() {
            @Override
            public void onDaySelected(DateTime date) {
                presenter.reloadSchedule();
            }
        });

        presenter.onCreateView(savedInstanceState);
        return view;
    }

    @Override
    public void onResume() {

        super.onResume();
        if (listState != null){
            ListView scheduleList = (ListView)view.findViewById(R.id.list_schedule);
            scheduleList.onRestoreInstanceState(listState);
        }
        listState = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
        //save list state
        ListView scheduleList = (ListView)view.findViewById(R.id.list_schedule);
        listState = scheduleList.onSaveInstanceState();
        outState.putParcelable(LIST_STATE, listState);
    }

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        // This is somewhat like [[NSNotificationCenter defaultCenter] removeObserver:name:object:]
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void setStartAndEndDateWithParts(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
        Ranger ranger = (Ranger) view.findViewById(R.id.ranger_summit);
        ranger.setStartAndEndDateWithParts(startYear, startMonth, startDay, endYear, endMonth, endDay);
    }

    @Override
    public void setStartAndEndDateWithDisabledDates(DateTime startDate, DateTime endDate, List<DateTime> disabledDates) {
        Ranger ranger = (Ranger) view.findViewById(R.id.ranger_summit);
        ranger.setStartAndEndDateWithDisabledDates(startDate, endDate, disabledDates);
    }

    @Override
    public List<ScheduleItemDTO> getEvents() {
        return events;
    }

    @Override
    public void setEvents(List<ScheduleItemDTO> events) {
        this.events = events;
        scheduleListAdapter.clear();
        scheduleListAdapter.addAll(events);
        TextView listEmptyMessageTextView = (TextView)view.findViewById(R.id.list_empty_message);
        listEmptyMessageTextView.setVisibility(events.size() > 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public DateTime getSelectedDate() {
        Ranger ranger = (Ranger) view.findViewById(R.id.ranger_summit);
        return ranger.getSelectedDate();
    }

    @Override
    public void setSelectedDate(int day){
        Ranger ranger = (Ranger) view.findViewById(R.id.ranger_summit);
        ranger.setSelectedDay(day, true);
    }

    @Override
    public void reloadSchedule() {
        Ranger ranger = (Ranger) view.findViewById(R.id.ranger_summit);
        ((LinearLayout)ranger.getParent()).setVisibility(View.VISIBLE);
        scheduleListAdapter.notifyDataSetChanged();
    }

    @Override
    public void setDisabledDates(List<DateTime> disabledDates) {
        Ranger ranger = (Ranger) view.findViewById(R.id.ranger_summit);
        ranger.setDisabledDates(disabledDates);
    }

    private class ScheduleListAdapter extends ArrayAdapter<ScheduleItemDTO> {

        public ScheduleListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_schedule, parent, false);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.showEventDetail(position);
                }
            });

            final ScheduleItemView scheduleItemView = new ScheduleItemView(convertView);

            ImageButton scheduleStatus = (ImageButton)convertView.findViewById(R.id.item_schedule_imagebutton_scheduled);
            scheduleStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.toggleScheduleStatus(scheduleItemView, position);
                }
            });

            presenter.buildItem(scheduleItemView, position);

            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        };
    }
}