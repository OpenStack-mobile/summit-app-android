package org.openstack.android.summit.modules.track_schedule.user_interface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.user_interface.ScheduleFragment;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public class TrackScheduleFragment  extends ScheduleFragment<ITrackSchedulePresenter> implements ITrackScheduleFragment {
    private NamedDTO track;

    public TrackScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void setTrack(NamedDTO track) {
        this.track = track;
    }

    public void setTitle(String title) {
        TextView nameTextView = (TextView)view.findViewById(R.id.track_schedule_name);
        nameTextView.setText(title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        presenter.setTrack(track);
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        getActivity().setTitle("TRACK");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_schedule, container, false);
        this.view = view;
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}