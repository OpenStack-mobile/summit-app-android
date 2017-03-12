package org.openstack.android.summit.common.user_interface;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.andressantibanez.ranger.Ranger;

import org.joda.time.DateTime;
import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.user_interface.recycler_view.DividerItemDecoration;
import org.openstack.android.summit.common.user_interface.schedule_list.ScheduleListAdapter;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by Claudio Redi on 12/29/2015.
 */
public class ScheduleFragment<P extends ISchedulePresenter>
        extends BaseFragment<P>
        implements IScheduleView {

    protected ScheduleListAdapter scheduleListAdapter;
    protected RecyclerView scheduleList;
    protected Ranger ranger;
    protected ToggleButton nowButton;
    protected LinearLayoutManager layoutManager = null;
    protected Integer     selectedDay           = null;
    private static final String SELECTED_DAY    = "ScheduleFragment.SCHEDULE_SELECTED_DAY";
    // list position state
    private static final String LIST_POSITION   = "ScheduleFragment.LIST_POSITION";
    protected int listPosition                  = -1;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setView(this);
        if(savedInstanceState != null){
            selectedDay = savedInstanceState.getInt(SELECTED_DAY, 0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        clearNowButtonListener();
        if(layoutManager != null)
            listPosition = layoutManager.findFirstVisibleItemPosition();
    }

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        // This is somewhat like [[NSNotificationCenter defaultCenter] removeObserver:name:object:]
        super.onDestroy();
        presenter.onDestroy();
        clearNowButtonListener();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        scheduleList   = (RecyclerView)view.findViewById(R.id.list_schedule);
        layoutManager  = new LinearLayoutManager(getContext());

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        scheduleList.setLayoutManager(layoutManager);
        scheduleList.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);

        scheduleList.addItemDecoration(itemDecoration);
        scheduleListAdapter  = new ScheduleListAdapter(presenter);

        scheduleList.setAdapter(new AlphaInAnimationAdapter(scheduleListAdapter));
        scheduleList.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));

        ranger    = (Ranger) view.findViewById(R.id.ranger_summit);
        nowButton = (ToggleButton) view.findViewById(R.id.now_filter_button);

        presenter.onCreateView(savedInstanceState);

        return view;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if (selectedDay != null && selectedDay > 0 ){
            ranger.setSelectedDay(selectedDay, false);
        }
        setNowButtonListener();
        ranger.setDayViewOnClickListener(date -> {
            selectedDay = date.getDayOfMonth();
            presenter.reloadSchedule();
        });
        presenter.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setNowButtonListener(){
        nowButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            presenter.setHidePastTalks(isChecked);
        });
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
        if(layoutManager != null)
            listPosition = layoutManager.findFirstVisibleItemPosition();
        //save list state ( if visible)
        if(view != null) {
            outState.putInt(SELECTED_DAY, ranger.getSelectedDay());
        }
        if(selectedDay != null){
            outState.putInt(SELECTED_DAY, selectedDay);
        }
        outState.putInt(LIST_POSITION, listPosition);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onViewStateRestored (savedInstanceState);
        if(savedInstanceState != null){
            selectedDay  = savedInstanceState.getInt(SELECTED_DAY, 0);
            listPosition = savedInstanceState.getInt(LIST_POSITION, -1);
        }

        presenter.onRestoreInstanceState(savedInstanceState);
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
        int formerSelectedDay =  ranger.getSelectedDay();
        ranger.setStartAndEndDateWithDisabledDates(startDate, endDate, disabledDates);
        if(disabledDates.size() > 0 && formerSelectedDay == 0){
            while (startDate.isBefore(endDate)) {
                if(!disabledDates.contains(startDate)){
                    formerSelectedDay = startDate.getDayOfMonth();
                    break;
                }
                startDate = startDate.plusDays(1);
            }
        }
        ranger.setSelectedDay(formerSelectedDay, false);
    }


    @Override
    public void setEvents(final List<ScheduleItemDTO> events) {

        scheduleListAdapter.clear();
        scheduleListAdapter.addAll(events);

        // reset position
        if(events.size() > 0)
            scheduleList.scrollToPosition(0);

        showEmptyMessage(events.size() == 0 );

        // if we have a former state, set it
        if (layoutManager != null && listPosition != -1) {
            scheduleList.scrollToPosition(listPosition);
            listPosition = -1;
        }
    }

    @Override
    public void showEmptyMessage(boolean show){
        TextView listEmptyMessageTextView = (TextView) view.findViewById(R.id.list_empty_message);
        listEmptyMessageTextView.setVisibility(show? View.VISIBLE: View.GONE );
    }

    @Override
    public DateTime getSelectedDate() {
        try {
            Ranger ranger = (Ranger) view.findViewById(R.id.ranger_summit);
            if (ranger == null) return null;
            return ranger.getSelectedDate();
        }
        catch (Exception ex){
            return null;
        }
    }

    @Override
    public void setSelectedDate(int day, boolean notifyListeners) {
        Ranger ranger = (Ranger) view.findViewById(R.id.ranger_summit);
        if(ranger == null) return;
        ranger.setSelectedDay(day, notifyListeners);
    }

    @Override
    public void setSelectedDate(int day){
        setSelectedDate(day, true);
    }

    @Override
    public void reloadSchedule() {
        Ranger ranger = (Ranger) view.findViewById(R.id.ranger_summit);
        if(ranger == null) return;
        ((LinearLayout)ranger.getParent()).setVisibility(View.VISIBLE);
    }

    @Override
    public void setDisabledDates(List<DateTime> disabledDates) {
        Ranger ranger = (Ranger) view.findViewById(R.id.ranger_summit);
        if(ranger == null) return;
        ranger.setDisabledDates(disabledDates);
    }
}