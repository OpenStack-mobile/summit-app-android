package org.openstack.android.summit.modules.track_schedule.user_interface;

import android.os.Bundle;

import org.joda.time.DateTime;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.user_interface.IScheduleItemViewBuilder;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.SchedulePresenter;
import org.openstack.android.summit.modules.track_schedule.ITrackScheduleWireframe;
import org.openstack.android.summit.modules.track_schedule.business_logic.ITrackScheduleInteractor;
import org.openstack.android.summit.modules.track_schedule.user_interface.ITrackSchedulePresenter;
import org.openstack.android.summit.modules.track_schedule.user_interface.TrackScheduleFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public class TrackSchedulePresenter extends SchedulePresenter<TrackScheduleFragment, ITrackScheduleInteractor, ITrackScheduleWireframe> implements ITrackSchedulePresenter {
    private NamedDTO track;

    public TrackSchedulePresenter(ITrackScheduleInteractor interactor, ITrackScheduleWireframe wireframe, IScheduleablePresenter scheduleablePresenter, IScheduleItemViewBuilder scheduleItemViewBuilder) {
        super(interactor, wireframe, scheduleablePresenter, scheduleItemViewBuilder);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        view.setTitle(track.getName());
        super.onCreate(savedInstanceState);
    }

    @Override
    protected List<ScheduleItemDTO> getScheduleEvents(DateTime startDate, DateTime endDate, ITrackScheduleInteractor interactor) {
        ArrayList<Integer> tracks = new ArrayList<>();
        tracks.add(track.getId());
        List<ScheduleItemDTO> summitEvents = interactor.getScheduleEvents(
                startDate.toDate(), endDate.toDate(), null, null, tracks, null, null);

        return summitEvents;
    }

    @Override
    public void setTrack(NamedDTO track) {
        this.track = track;
    }
}