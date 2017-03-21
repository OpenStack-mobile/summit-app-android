package org.openstack.android.summit.modules.personal_schedule.user_interface;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.user_interface.ScheduleFragment;
import org.openstack.android.summit.common.user_interface.tabs.FragmentLifecycle;

import java.util.List;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public class PersonalScheduleFragment
        extends ScheduleFragment<IPersonalSchedulePresenter>
        implements IPersonalScheduleView, FragmentLifecycle {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_schedule, container, false);
        this.view = view;
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void removeItem(final int position) {
        scheduleListAdapter.removeItem(position);
        showEmptyMessage(scheduleListAdapter.isEmpty());
    }

    @Override
    public void enableListView(boolean enable) {
        scheduleList.setEnabled(enable);
    }

    @Override
    public void setEvents(final List<ScheduleItemDTO> events){
        Log.d(Constants.LOG_TAG, "PersonalScheduleFragment.setEvents");
        super.setEvents(events);
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {
        if(presenter == null) return;
        presenter.setRangerState();
        presenter.reloadSchedule();
    }
}
