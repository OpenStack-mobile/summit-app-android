package org.openstack.android.summit.common.user_interface;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.andressantibanez.ranger.Ranger;

import org.joda.time.DateTime;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;

import java.util.List;


/**
 * Created by Claudio Redi on 12/29/2015.
 */
public class ScheduleFragment<P extends ISchedulePresenter> extends BaseFragment<P> implements IScheduleView {

    protected ScheduleListAdapter scheduleListAdapter;
    protected List<ScheduleItemDTO> events;
    protected ListView scheduleList;
    protected Ranger ranger;
    protected ToggleButton nowButton;

    protected Parcelable listState            = null;
    private static final String LIST_STATE    = "SCHEDULE_LIST_STATE";
    protected Integer     selectedDay         = null;
    private static final String SELECTED_DAY  = "SCHEDULE_SELECTED_DAY";
    private CompoundButton.OnCheckedChangeListener toggleListener = (buttonView, isChecked) -> {
        presenter.setHidePastTalks(isChecked);
    };

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setView(this);
        if(savedInstanceState != null){
            listState   = savedInstanceState.getParcelable(LIST_STATE);
            selectedDay = savedInstanceState.getInt(SELECTED_DAY, 0);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        scheduleList        = (ListView)view.findViewById(R.id.list_schedule);
        scheduleListAdapter = new ScheduleListAdapter(getContext());

        scheduleList.setOnScrollListener(new AbsListView.OnScrollListener(){
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //stop animations on scrolling
                if(scrollState == 0)
                {
                    scheduleListAdapter.enableAnimations(true);
                }
                else{
                    scheduleListAdapter.enableAnimations(false);
                }
             }
        });

        scheduleList.setAdapter(scheduleListAdapter);

        ranger = (Ranger) view.findViewById(R.id.ranger_summit);
        ranger.setDayViewOnClickListener(date -> presenter.reloadSchedule());

        nowButton = (ToggleButton) view.findViewById(R.id.now_filter_button);
        nowButton.setOnCheckedChangeListener(toggleListener);

        presenter.onCreateView(savedInstanceState);
        return view;
    }

    @Override
    public void setNowButtonListener(){
        nowButton.setOnCheckedChangeListener(toggleListener);
    }

    @Override
    public void clearNowButtonListener(){
        nowButton.setOnCheckedChangeListener(null);
    }

    @Override
    public boolean getNowButtonState() {
        if(nowButton == null) return false;
        return nowButton.isChecked();
    }

    @Override
    public void setNowButtonVisibility(int visibility) {
        if(nowButton == null) return;
        nowButton.setVisibility(visibility);
    }

    @Override
    public void setNowButtonState(boolean isChecked){
        if(nowButton == null) return;
        nowButton.setChecked(isChecked);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listState != null){
            scheduleList.onRestoreInstanceState(listState);
        }
        if (selectedDay != null && selectedDay > 0 ){
            ranger.setSelectedDay(selectedDay, true);
        }
        listState   = null;
        selectedDay = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
        //save list state ( if visible)
        if(view != null) {
            listState = scheduleList.onSaveInstanceState();
            outState.putParcelable(LIST_STATE, listState);
            outState.putInt(SELECTED_DAY, ranger.getSelectedDay());
        }
        if(listState != null){
            outState.putParcelable(LIST_STATE, listState);
        }
        if(selectedDay != null){
            outState.putInt(SELECTED_DAY, selectedDay);
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onViewStateRestored (savedInstanceState);
        if(savedInstanceState != null){
            listState   = savedInstanceState.getParcelable(LIST_STATE);
            selectedDay = savedInstanceState.getInt(SELECTED_DAY, 0);
        }
        presenter.onRestoreInstanceState(savedInstanceState);
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
        if(ranger == null) return;
        ranger.setStartAndEndDateWithParts(startYear, startMonth, startDay, endYear, endMonth, endDay);
    }

    @Override
    public void setStartAndEndDateWithDisabledDates(DateTime startDate, DateTime endDate, List<DateTime> disabledDates) {
        Ranger ranger = (Ranger) view.findViewById(R.id.ranger_summit);
        if(ranger == null) return;
        ranger.setStartAndEndDateWithDisabledDates(startDate, endDate, disabledDates);
    }

    @Override
    public List<ScheduleItemDTO> getEvents() {
        return events;
    }

    @Override
    public void setEvents(final List<ScheduleItemDTO> events) {
        scheduleListAdapter.clear();
        scheduleListAdapter.addAll(events);
        showEmptyMessage(events.size() == 0 );
    }

    @Override
    public void showEmptyMessage(boolean show){
        TextView listEmptyMessageTextView = (TextView) view.findViewById(R.id.list_empty_message);
        listEmptyMessageTextView.setVisibility(show? View.VISIBLE: View.GONE );
    }


    @Override
    public DateTime getSelectedDate() {
        Ranger ranger = (Ranger) view.findViewById(R.id.ranger_summit);
        if(ranger == null) return null;
        return ranger.getSelectedDate();
    }

    @Override
    public void setSelectedDate(int day){
        Ranger ranger = (Ranger) view.findViewById(R.id.ranger_summit);
        if(ranger == null) return;
        ranger.setSelectedDay(day, true);
    }

    @Override
    public void reloadSchedule() {
        Ranger ranger = (Ranger) view.findViewById(R.id.ranger_summit);
        if(ranger == null) return;
        ((LinearLayout)ranger.getParent()).setVisibility(View.VISIBLE);
        scheduleListAdapter.notifyDataSetChanged();
    }


    @Override
    public void setDisabledDates(List<DateTime> disabledDates) {
        Ranger ranger = (Ranger) view.findViewById(R.id.ranger_summit);
        if(ranger == null) return;
        ranger.setDisabledDates(disabledDates);
    }

    protected class ScheduleListAdapter extends ArrayAdapter<ScheduleItemDTO> {

        private static final int AnimationDuration = 300;
        private int lastPosition                   = -1;
        private boolean areAnimationsEnabled       = true;

        public ScheduleListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        public void enableAnimations(boolean enable){
            areAnimationsEnabled = enable;
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
            if(areAnimationsEnabled) {
                Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPosition) ? R.anim.slide_from_top : R.anim.slide_to_top);
                animation.setDuration(AnimationDuration);
                convertView.startAnimation(animation);
                animation = null;
            }
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        };

        @Override
        public long getItemId(int position) {
            try {
                return getItem(position).getId();
            }
            catch(IndexOutOfBoundsException ex){
                return -1;
            }
        }
    }
}