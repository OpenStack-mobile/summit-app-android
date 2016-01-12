package org.openstack.android.summit.modules.track_list.user_interface;

import android.os.Bundle;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.common.user_interface.ISimpleListItemView;
import org.openstack.android.summit.modules.track_list.ITrackListWireframe;
import org.openstack.android.summit.modules.track_list.business_logic.ITrackListInteractor;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/12/2016.
 */
public class TrackListPresenter extends BasePresenter implements ITrackListPresenter {

    @Inject
    public TrackListPresenter(ITrackListInteractor interactor, ITrackListWireframe wireframe) {
        this.interactor = interactor;
        this.wireframe = wireframe;
    }

    private ITrackListWireframe wireframe;
    private ITrackListInteractor interactor;
    private TrackListFragment view;
    private List<NamedDTO> tracks;

    @Override
    public void setView(TrackListFragment view) {
        this.view = view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        tracks = interactor.getTracks();
        view.setTracks(tracks);
        view.reloadData();
    }

    @Override
    public void showTrackEvents(int position) {
        NamedDTO track = tracks.get(position);
        wireframe.showTrackSchedule(track, view.getActivity());
    }

    @Override
    public void buildItem(ISimpleListItemView trackListItemView, int position) {
        NamedDTO track = tracks.get(position);
        trackListItemView.setName(track.getName());
    }
}
