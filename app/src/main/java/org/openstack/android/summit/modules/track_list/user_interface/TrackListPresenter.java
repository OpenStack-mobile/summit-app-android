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

    @Inject
    public TrackListPresenter(ITrackListInteractor interactor, ITrackListWireframe wireframe, IScheduleFilter scheduleFilter) {
        super(interactor, wireframe);
        this.scheduleFilter = scheduleFilter;
    }

    private List<TrackDTO> tracks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        init();
    }

    private void init() {
        List<Integer> filtersOnTrackGroups = (List<Integer>)(List<?>) scheduleFilter.getSelections().get(FilterSectionType.TrackGroup);
        tracks = interactor.getTracks(filtersOnTrackGroups);
        view.setTracks(tracks);
    }

    @Override
    public void showTrackEvents(int position) {
        NamedDTO track = tracks.get(position);
        wireframe.showTrackSchedule(track, view);
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
