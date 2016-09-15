package org.openstack.android.summit.modules.speaker_presentations.user_interface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.user_interface.ScheduleFragment;

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
        view = inflater.inflate(R.layout.fragment_personal_schedule, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        presenter.onCreateView(savedInstanceState);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onViewStateRestored (savedInstanceState);
        presenter.onRestoreInstanceState(savedInstanceState);
    }
}
