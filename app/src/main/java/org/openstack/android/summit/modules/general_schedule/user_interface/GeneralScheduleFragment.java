package org.openstack.android.summit.modules.general_schedule.user_interface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.user_interface.ScheduleFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeneralScheduleFragment extends ScheduleFragment<IGeneralSchedulePresenter> implements IGeneralScheduleView {

    public GeneralScheduleFragment() {
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
        view = inflater.inflate(R.layout.fragment_general_schedule, container, false);

        Button retryButton = (Button)view.findViewById(R.id.general_schedule_retry_button);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onResume();
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void reloadSchedule() {
        LinearLayout eventListContainer = (LinearLayout)view.findViewById(R.id.general_schedule_list_container);
        eventListContainer.setVisibility(View.VISIBLE);
        super.reloadSchedule();
    }

    @Override
    public void toggleEventList(boolean show) {
        LinearLayout eventListContainer = (LinearLayout)view.findViewById(R.id.general_schedule_list_container);
        eventListContainer.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void toggleNoConnectivityMessage(boolean show) {
        LinearLayout noConnectivityLayout = (LinearLayout)view.findViewById(R.id.general_schedule_no_connectivity_container);
        noConnectivityLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
