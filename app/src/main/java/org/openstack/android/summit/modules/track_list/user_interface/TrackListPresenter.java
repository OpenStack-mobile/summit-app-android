package org.openstack.android.summit.modules.track_list.user_interface;

import android.os.Bundle;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.TrackDTO;
import org.openstack.android.summit.common.IScheduleFilter;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.common.user_interface.ISimpleListItemView;
import org.openstack.android.summit.modules.general_schedule_filter.user_interface.FilterSectionType;
import org.openstack.android.summit.modules.track_list.ITrackListWireframe;
import org.openstack.android.summit.modules.track_list.business_logic.ITrackListInteractor;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public class TrackListPresenter extends BasePresenter<ITrackListView, ITrackListInteractor, ITrackListWireframe> implements ITrackListPresenter {
    private IScheduleFilter scheduleFilter;
    private List<TrackDTO> tracks;

    @Inject
    public TrackListPresenter(ITrackListInteractor interactor, ITrackListWireframe wireframe, IScheduleFilter scheduleFilter) {
        super(interactor, wireframe);
        this.scheduleFilter = scheduleFilter;
    }

    @Override
    public void onPause() {
        //empty, to avoid calling stop polling. This is because tab strip causes some issues with that
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        init();
    }

    private void init() {
        List<Integer> filtersOnTracks = (List<Integer>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.Tracks);
        tracks = interactor.getTracksById(filtersOnTracks);
        view.setTracks(tracks);
    }

    @Override
    public void showTrackEvents(int position) {
        NamedDTO track = tracks.get(position);
        wireframe.showTrackSchedule(track.getId(), view);
    }

    @Override
    public void buildItem(ISimpleListItemView trackListItemView, int position) {
        TrackDTO track = tracks.get(position);
        trackListItemView.setName(track.getName());
        trackListItemView.setColor(track.getTrackGroup() != null ? track.getTrackGroup().getColor() : null);
    }

    @Override
    public void reloadData() {
        if (tracks.size() == 0) {
            init();
        }
    }
}
