package org.openstack.android.openstacksummit.modules.event_detail.user_interface;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.openstack.android.openstacksummit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailFragment extends Fragment {


    public EventDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_detail, container, false);
    }
}
