package org.openstack.android.summit.modules.track_list.user_interface;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.user_interface.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrackListFragment extends BaseFragment {


    public TrackListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_track_list, container, false);
    }

    @Override
    public void onResume() {
        getActivity().setTitle("EVENTS");
        super.onResume();
    }
}
