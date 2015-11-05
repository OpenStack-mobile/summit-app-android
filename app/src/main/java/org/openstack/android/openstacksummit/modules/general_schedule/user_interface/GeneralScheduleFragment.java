package org.openstack.android.openstacksummit.modules.general_schedule.user_interface;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.openstack.android.openstacksummit.R;
import org.openstack.android.openstacksummit.common.user_interface.BaseFragment;
import org.openstack.android.openstacksummit.dagger.HasComponent;
import org.openstack.android.openstacksummit.dagger.components.GeneralScheduleComponent;
import org.openstack.android.openstacksummit.dagger.modules.GeneralScheduleModule;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GeneralScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GeneralScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeneralScheduleFragment extends BaseFragment {

    @Inject
    GeneralSchedulePresenter presenter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GeneralScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GeneralScheduleFragment newInstance() {
        GeneralScheduleFragment fragment = new GeneralScheduleFragment();
        return fragment;
    }

    public GeneralScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent().inject(this);
        this.presenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_general_schedule, container, false);
    }
}
