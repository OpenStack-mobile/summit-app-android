package org.openstack.android.summit.modules.track_schedule.user_interface;

import android.os.Bundle;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.user_interface.IScheduleItemViewBuilder;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.SchedulePresenter;
import org.openstack.android.summit.modules.track_schedule.ITrackScheduleWireframe;
import org.openstack.android.summit.modules.track_schedule.business_logic.ITrackScheduleInteractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public class TrackSchedulePresenter extends SchedulePresenter<ITrackScheduleView, ITrackScheduleInteractor, ITrackScheduleWireframe> implements ITrackSchedulePresenter {
    private Integer trackId;
    private NamedDTO track;

    public TrackSchedulePresenter(ITrackScheduleInteractor interactor, ITrackScheduleWireframe wireframe, IScheduleablePresenter scheduleablePresenter, IScheduleItemViewBuilder scheduleItemViewBuilder) {
        super(interactor, wireframe, scheduleablePresenter, scheduleItemViewBuilder);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            trackId = savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_TRACK);
        }
        else {
            trackId = wireframe.getParameter(Constants.NAVIGATION_PARAMETER_TRACK, Integer.class);
        }

        track = interactor.getTrack(trackId);
        view.setTrack(track.getName());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.NAVIGATION_PARAMETER_TRACK, trackId);
    }

    @Override
    protected List<ScheduleItemDTO> getScheduleEvents(DateTime startDate, DateTime endDate, ITrackScheduleInteractor interactor) {
        ArrayList<Integer> tracks = new ArrayList<>();
        tracks.add(trackId);
        List<ScheduleItemDTO> summitEvents = interactor.getScheduleEvents(
                startDate.toDate(), endDate.toDate(), null, null, tracks, null, null);

        return summitEvents;
    }
}