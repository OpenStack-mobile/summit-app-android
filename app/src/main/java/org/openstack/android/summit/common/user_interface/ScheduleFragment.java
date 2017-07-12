package org.openstack.android.summit.common.user_interface;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andressantibanez.ranger.Ranger;

import org.joda.time.DateTime;
import org.openstack.android.summit.R2;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.user_interface.recycler_view.DividerItemDecoration;
import org.openstack.android.summit.common.user_interface.schedule_list.ScheduleListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;

/**
 * Created by Claudio Redi on 12/29/2015.
 */
public class ScheduleFragment<P extends ISchedulePresenter>
        extends BaseFragment<P>
        implements IScheduleView {

    protected ScheduleListAdapter scheduleListAdapter;
    protected Unbinder unbinder;

    @BindView(R2.id.list_schedule)
    protected RecyclerView scheduleList;

    @BindView(R2.id.ranger_summit)
    protected Ranger ranger;

    @BindView(R2.id.now_filter_button)
    protected Button nowButton;

    @BindView(R2.id.list_empty_message)
    protected TextView listEmptyMessageTextView;

    protected LinearLayoutManager layoutManager = null;
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
    }

    @Override
    public void onStop() {
        super.onStop();
        if(layoutManager != null)
            listPosition = layoutManager.findFirstVisibleItemPosition();
    }

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        // This is somewhat like [[NSNotificationCenter defaultCenter] removeObserver:name:object:]
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onPause() {
        // Unregister since the activity is about to be closed.
        // This is somewhat like [[NSNotificationCenter defaultCenter] removeObserver:name:object:]
        super.onPause();
        presenter.onPause();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        unbinder       = ButterKnife.bind(this, view);
        layoutManager  = new LinearLayoutManager(getContext());

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        scheduleList.setLayoutManager(layoutManager);
        scheduleList.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);

        scheduleList.addItemDecoration(itemDecoration);
        scheduleListAdapter  = new ScheduleListAdapter(presenter);

        scheduleList.setItemAnimator(new SlideInDownAnimator());
        scheduleList.getItemAnimator().setRemoveDuration(300);
        scheduleList.getItemAnimator().setAddDuration(300);
        scheduleList.getItemAnimator().setChangeDuration(500);

        scheduleList.setAdapter(scheduleListAdapter);

        presenter.onCreateView(savedInstanceState);

        ranger.setDayViewOnClickListener(date -> {
            if(scheduleList == null) return;
            scheduleList.setItemAnimator(new SlideInDownAnimator());
            scheduleList.getItemAnimator().setRemoveDuration(300);
            scheduleList.getItemAnimator().setAddDuration(300);
            scheduleList.getItemAnimator().setChangeDuration(500);
            presenter.reloadSchedule(date.getDayOfMonth());
        });

        nowButton.setOnClickListener(view1 -> presenter.gotoNowOnSchedule());

        return view;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        presenter.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setNowButtonVisibility(int visibility) {
        if(nowButton == null) return;
        nowButton.setVisibility(visibility);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(layoutManager != null)
            listPosition = layoutManager.findFirstVisibleItemPosition();
        outState.putInt(LIST_POSITION, listPosition);
        presenter.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onViewStateRestored (savedInstanceState);
        if(savedInstanceState != null){
            listPosition = savedInstanceState.getInt(LIST_POSITION, -1);
        }
        presenter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void setStartAndEndDateWithParts(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
        if(ranger == null) return;
        ranger.setStartAndEndDateWithParts(startYear, startMonth, startDay, endYear, endMonth, endDay);
    }

    @Override
    public void setStartAndEndDateWithDisabledDates(DateTime startDate, DateTime endDate, List<DateTime> disabledDates) {
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
        if(formerSelectedDay > 0)
            ranger.setSelectedDay(formerSelectedDay, false);
    }


    @Override
    public void setEvents(final List<ScheduleItemDTO> events) {
        if(scheduleList == null) return;
        if(scheduleListAdapter == null) return;

        scheduleListAdapter.clear();

        scheduleListAdapter.addAll(events);

        // reset position
        if(events.size() > 0)
            layoutManager.scrollToPositionWithOffset(0, 0);

        showEmptyMessage(events.size() == 0 );

        // if we have a former state, set it
        if (layoutManager != null && listPosition != -1) {
            layoutManager.scrollToPositionWithOffset(listPosition, 0);
            listPosition = -1;
        }
    }

    @Override
    public void setListPosition(int newPosition){
        if(scheduleList != null)
            scheduleList.setItemAnimator(null);
        //scheduleList.scrollToPosition(listPosition);
        if(layoutManager != null)
            layoutManager.scrollToPositionWithOffset(newPosition, 0);
    }

    @Override
    public void showEmptyMessage(boolean show){
        if(listEmptyMessageTextView == null) return;
        listEmptyMessageTextView.setVisibility(show? View.VISIBLE: View.GONE );
    }

    @Override
    public DateTime getSelectedDate() {
        try {
            if (ranger == null) return null;
            return ranger.getSelectedDate();
        }
        catch (Exception ex){
            return null;
        }
    }

    @Override
    public int getSelectedDay() {
        try {
            if (ranger == null) return 0;
            return ranger.getSelectedDay();
        }
        catch (Exception ex){
            return 0;
        }
    }

    @Override
    public void setSelectedDate(int day, boolean notifyListeners) {
        if(ranger == null) return;
        ranger.setSelectedDay(day, notifyListeners);
    }

    @Override
    public void setSelectedDate(int day){
        setSelectedDate(day, true);
    }

    @Override
    public void reloadSchedule() {
        if(ranger == null) return;
        ((LinearLayout)ranger.getParent()).setVisibility(View.VISIBLE);
    }

    @Override
    public void setDisabledDates(List<DateTime> disabledDates) {
        if(ranger == null) return;
        ranger.setDisabledDates(disabledDates);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        if(unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }

    @Override
    public void reloadItem(int position) {
        scheduleListAdapter.notifyItemChanged(position);
    }
}