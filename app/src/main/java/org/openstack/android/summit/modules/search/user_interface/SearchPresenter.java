package org.openstack.android.summit.modules.search.user_interface;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.openstack.android.summit.common.Constants;
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

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Claudio Redi on 1/14/2016.
 */
public class SearchPresenter
        extends BasePresenter<ISearchView, ISearchInteractor, ISearchWireframe>
        implements ISearchPresenter
{

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
        if (savedInstanceState != null) {
            searchTerm = savedInstanceState.getString(Constants.NAVIGATION_PARAMETER_SEARCH_TERM);
        }
        else {
            searchTerm = wireframe.getParameter(Constants.NAVIGATION_PARAMETER_SEARCH_TERM, String.class);
        }
    }

    @Override
    public void onResume() {
        search(searchTerm);
        view.setSearchTerm(searchTerm);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.NAVIGATION_PARAMETER_SEARCH_TERM, searchTerm);
    }

    @Override
    public void search(final String searchTerm) {
        view.showActivityIndicator();

        this.searchTerm = searchTerm;

        Runnable searchRunnable = new Runnable() {
            @Override
            public void run() {
                events = interactor.getEventsBySearchTerm(searchTerm);
                tracks = interactor.getTracksBySearchTerm(searchTerm);
                speakers = interactor.getSpeakersBySearchTerm(searchTerm);

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
        };
        searchRunnable.run();
    }

    @Override
    public void showTrackSchedule(int position) {
        NamedDTO track = tracks.get(position);
        wireframe.showTrackSchedule(track, view);
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
        PersonListItemDTO speaker = speakers.get(position);
        wireframe.showSpeakerProfile(speaker.getId(), view);
    }

    @Override
    public void buildItem(IScheduleItemView scheduleItemView, int position) {
        ScheduleItemDTO scheduleItemDTO = events.get(position);
        scheduleItemViewBuilder.build(
                scheduleItemView,
                scheduleItemDTO,
                interactor.isMemberLoggedIn(),
                interactor.isMemberLoggedInAndConfirmedAttendee(),
                interactor.isEventScheduledByLoggedMember(scheduleItemDTO.getId()),
                interactor.isEventFavoriteByLoggedMember(scheduleItemDTO.getId()),
                true,
                interactor.shouldShowVenues()
        );
    }

    @Override
    public void toggleScheduleStatus(IScheduleItemView scheduleItemView, int position) {
        ScheduleItemDTO scheduleItemDTO = events.get(position);

        IInteractorAsyncOperationListener<Void> interactorAsyncOperationListener = new InteractorAsyncOperationListener<Void>() {
            @Override
            public void onError(String message) {
                view.showErrorMessage(message);
            }
        };

        scheduleablePresenter.toggleScheduledStatusForEvent(scheduleItemDTO, scheduleItemView, interactor, interactorAsyncOperationListener);
    }

    @Override
    public void toggleFavoriteStatus(IScheduleItemView scheduleItemView, int position) {
        ScheduleItemDTO scheduleItemDTO = events.get(position);
        // get former state
        boolean formerFavoriteState     = scheduleItemView.getFavorite();
        scheduleablePresenter
                .toggleFavoriteStatusForEvent(scheduleItemDTO, scheduleItemView, interactor)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe
                        (
                                (res) -> {
                                    Toast.makeText(view.getApplicationContext(), formerFavoriteState ?
                                                    "Removed from Favorites" : "Added to Favorites",
                                            Toast.LENGTH_SHORT).show();
                                },
                                (ex) -> {
                                    scheduleItemView.setFavorite(formerFavoriteState);
                                    if(ex != null) {
                                        Log.d(Constants.LOG_TAG, ex.getMessage());
                                        view.showErrorMessage(ex.getMessage());
                                        return;
                                    }
                                    view.showErrorMessage("Server Error");
                                }

                        );
    }

    @Override
    public void showEventDetail(int position) {
        ScheduleItemDTO scheduleItemDTO = events.get(position);
        wireframe.showEventDetail(scheduleItemDTO.getId(), view);
    }
}
