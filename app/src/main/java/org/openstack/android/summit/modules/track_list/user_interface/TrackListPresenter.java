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
public class TrackListPresenter extends BasePresenter<TrackListFragment, ITrackListInteractor, ITrackListWireframe> implements ITrackListPresenter {

    @Inject
    public TrackListPresenter(ITrackListInteractor interactor, ITrackListWireframe wireframe) {
        super(interactor, wireframe);
    }

    private List<NamedDTO> tracks;

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
