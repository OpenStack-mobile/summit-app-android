package org.openstack.android.summit.modules.level_schedule.user_interface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.ScheduleFragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public class LevelScheduleFragment extends ScheduleFragment<ILevelSchedulePresenter> {
    private String level;

    public LevelScheduleFragment() {
        // Required empty public constructor
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        presenter.setLevel(level);
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        getActivity().setTitle(level.toUpperCase());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // This is the home/back button
                break;
        }

        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_level_schedule, container, false);
        this.view = view;
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
