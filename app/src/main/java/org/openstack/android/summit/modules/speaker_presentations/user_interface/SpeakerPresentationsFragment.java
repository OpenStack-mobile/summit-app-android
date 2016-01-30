package org.openstack.android.summit.modules.speaker_presentations.user_interface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.ScheduleFragment;
import org.openstack.android.summit.modules.personal_schedule.user_interface.IPersonalSchedulePresenter;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public class SpeakerPresentationsFragment extends ScheduleFragment<ISpeakerPresentationsPresenter> implements ISpeakerPresentationsView {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_schedule, container, false);
        this.view = view;
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
