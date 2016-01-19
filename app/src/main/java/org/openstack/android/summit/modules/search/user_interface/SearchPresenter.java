package org.openstack.android.summit.modules.search.user_interface;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.business_logic.IInteractorAsyncOperationListener;
import org.openstack.android.summit.common.business_logic.InteractorAsyncOperationListener;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.common.user_interface.IPersonItemView;
import org.openstack.android.summit.common.user_interface.IScheduleItemView;
import org.openstack.android.summit.common.user_interface.IScheduleItemViewBuilder;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.ISimpleListItemView;
import org.openstack.android.summit.modules.search.ISearchWireframe;
import org.openstack.android.summit.modules.search.business_logic.ISearchInteractor;

import java.util.List;

/**
 * Created by Claudio Redi on 1/14/2016.
 */
public class SearchPresenter extends BasePresenter<SearchFragment, ISearchInteractor, ISearchWireframe> implements ISearchPresenter {
    private String searchTerm;
    private List<ScheduleItemDTO> events;
    private List<NamedDTO> tracks;
    private List<PersonListItemDTO> speakers;
    private IScheduleItemViewBuilder scheduleItemViewBuilder;
    private IScheduleablePresenter scheduleablePresenter;

    public SearchPresenter(ISearchInteractor interactor, ISearchWireframe wireframe, IScheduleablePresenter scheduleablePresenter, IScheduleItemViewBuilder scheduleItemViewBuilder) {
        super(interactor, wireframe);
        this.scheduleablePresenter = scheduleablePresenter;
        this.scheduleItemViewBuilder = scheduleItemViewBuilder;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        search(searchTerm);
    }

    @Override
    public void search(final String searchTerm) {
        view.showActivityIndicator();

        this.searchTerm = searchTerm;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                events = interactor.getEventsBySearchTerm(searchTerm);
                tracks = interactor.getTracksBySearchTerm(searchTerm);
                speakers = interactor.getSpeakersBySearchTerm(searchTerm);

                view.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (events.size() > 0) {
                            view.showEvents(events);
                        }
                        else {
                            view.showNoResultsForEvents();
                        }

                        if (tracks.size() > 0) {
                            view.showTracks(tracks);
                        }
                        else {
                            view.showNoResultsForTracks();
                        }

                        if (speakers.size() > 0) {
                            view.showSpeakers(speakers);
                        }
                        else {
                            view.showNoResultsForSpeakers();
                        }

                        view.hideActivityIndicator();
                    }
                });
            }
        });

        thread.start();
    }

    @Override
    public void showTrackSchedule(int position) {
        NamedDTO track = tracks.get(position);
        wireframe.showTrackSchedule(track, view.getActivity());
    }

    @Override
    public void buildTrackItem(ISimpleListItemView simpleListItemView, int position) {
        NamedDTO track = tracks.get(position);
        simpleListItemView.setName(track.getName());
    }

    @Override
    public void buildSpeakerItem(IPersonItemView personItemView, int position) {
        PersonListItemDTO personListItemDTO = speakers.get(position);
        personItemView.setName(personListItemDTO.getName());
        personItemView.setTitle(personListItemDTO.getTitle());

        Uri uri = Uri.parse(personListItemDTO.getPictureUrl().replace("https", "http"));
        personItemView.setPictureUri(uri);
    }

    @Override
    public void showSpeakerProfile(int position) {

    }

    @Override
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public void buildScheduleItem(IScheduleItemView scheduleItemView, int position) {
        ScheduleItemDTO scheduleItemDTO = events.get(position);
        scheduleItemViewBuilder.build(
                scheduleItemView,
                scheduleItemDTO,
                interactor.isMemberLoggedIn(),
                interactor.isEventScheduledByLoggedMember(scheduleItemDTO.getId())
        );
    }

    @Override
    public void toggleScheduleStatus(IScheduleItemView scheduleItemView, int position) {
        ScheduleItemDTO scheduleItemDTO = events.get(position);
        scheduleablePresenter.toggleScheduledStatusForEvent(scheduleItemDTO, scheduleItemView, interactor);
    }

    @Override
    public void showEventDetail(int position) {

    }
}
