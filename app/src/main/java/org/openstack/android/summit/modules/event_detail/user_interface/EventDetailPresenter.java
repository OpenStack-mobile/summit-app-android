package org.openstack.android.summit.modules.event_detail.user_interface;

import android.net.Uri;
import android.os.Bundle;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.EventDetailDTO;
import org.openstack.android.summit.common.DTOs.FeedbackDTO;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.business_logic.IInteractorAsyncOperationListener;
import org.openstack.android.summit.common.business_logic.InteractorAsyncOperationListener;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.common.user_interface.FeedbackItemView;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.PersonItemView;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.event_detail.business_logic.IEventDetailInteractor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/21/2016.
 */
public class EventDetailPresenter extends BasePresenter<IEventDetailView, IEventDetailInteractor, IEventDetailWireframe> implements IEventDetailPresenter {

    private int eventId;
    private EventDetailDTO event;
    private IScheduleablePresenter scheduleablePresenter;
    private FeedbackDTO myFeedbackForEvent;
    private List<FeedbackDTO> feedbackList = new ArrayList<>();
    private boolean loadingFeedback;
    private boolean loadedAllFeedback;
    private int feedbackPage = 1;
    private int feedbackObjectsPerPage = 5;

    @Inject
    public EventDetailPresenter(IEventDetailInteractor interactor, IEventDetailWireframe wireframe, IScheduleablePresenter scheduleablePresenter) {
        super(interactor, wireframe);
        this.scheduleablePresenter = scheduleablePresenter;
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        if (savedInstanceState != null) {
            eventId = savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_EVENT_ID);
        }
        else {
            eventId = wireframe.getParameter(Constants.NAVIGATION_PARAMETER_EVENT_ID, Integer.class);
        }


        event = interactor.getEventDetail(eventId);
        myFeedbackForEvent = interactor.getMyFeedbackForEvent(eventId);
        view.setName(event.getName());
        view.setTrack(event.getTrack());
        view.setDate(event.getDateTime());
        view.setDescription(event.getEventDescription());
        view.setCredentials(event.getCredentials());
        view.setLevel(event.getLevel());
        view.setSponsors(event.getSponsors());
        view.setSpeakers(event.getSpeakers());
        view.setScheduled(interactor.isEventScheduledByLoggedMember(eventId));
        view.setIsScheduledStatusVisible(interactor.isMemberLoggedIn());
        view.setAllowNewFeedback(getAllowNewFeedback());
        view.setAverageRate((int) Math.round(event.getAverageRate()));
        view.setTags(event.getTags());
        view.hasMyFeedback(myFeedbackForEvent != null);

        if (interactor.shouldShowVenues()) {
            view.setLocation(event.getLocation());
        }
        else {
            String stringToRemove = " - " + event.getRoom();
            String locationWithoutRoom =  event.getLocation().replace(stringToRemove, "");
            view.setLocation(locationWithoutRoom);
        }

        if (myFeedbackForEvent != null) {
            view.setMyFeedbackRate(myFeedbackForEvent.getRate());
            view.setMyFeedbackReview(myFeedbackForEvent.getReview());
            view.setMyFeedbackDate(myFeedbackForEvent.getTimeAgo());
            view.setMyFeedbackOwner(myFeedbackForEvent.getOwner());
        }

        loadFeedback();
    }

    public void loadFeedback() {
        if (loadingFeedback || loadedAllFeedback) {
            return;
        }

        loadingFeedback = true;
        view.showFeedbackActivityIndicator();

        IInteractorAsyncOperationListener<List<FeedbackDTO>> interactorAsyncOperationListener = new InteractorAsyncOperationListener<List<FeedbackDTO>>() {
            @Override
            public void onSucceedWithData(List<FeedbackDTO> data) {
                super.onSucceedWithData(data);
                loadingFeedback = false;
                view.hideFeedbackActivityIndicator();
                List<FeedbackDTO> feedbackPageWithoutMe = new ArrayList<FeedbackDTO>();
                for (FeedbackDTO feedbackDTO : data) {
                    // TODO: if there are more than one owners with the same name, this could potencially fail
                    if (myFeedbackForEvent == null || (feedbackDTO.getOwner() != myFeedbackForEvent.getOwner())) {
                        feedbackPageWithoutMe.add(feedbackDTO);
                    }
                }

                feedbackList.addAll(feedbackPageWithoutMe);
                view.setOtherPeopleFeedback(feedbackList);
                //self.viewController.hasAnyFeedback = self.feedbackList.count > 0
                feedbackPage++;
                loadedAllFeedback = data.size() < feedbackObjectsPerPage;
                view.toggleLoadMore(!loadedAllFeedback);
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                loadingFeedback = false;
                view.hideFeedbackActivityIndicator();
                view.showErrorMessage(message);
            }
        };

        interactor.getFeedbackForEvent(eventId, feedbackPage, feedbackObjectsPerPage, interactorAsyncOperationListener);
    }

    @Override
    public void showVenueDetail() {
        wireframe.showEventDetailView(event.getVenueId(), view);
    }

    private boolean getAllowNewFeedback() {
        boolean allowFeedback = event.getAllowFeedback() &&
                event.getFinished() &&
                interactor.isMemberLoggedIn() &&
                myFeedbackForEvent == null;

        return allowFeedback;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.NAVIGATION_PARAMETER_EVENT_ID, eventId);
    }

    @Override
    public void buildSpeakerListItem(PersonItemView personItemView, int position) {
        PersonListItemDTO personListItemDTO = event.getSpeakers().get(position);
        personItemView.setName(personListItemDTO.getName());
        personItemView.setTitle(personListItemDTO.getTitle());

        Uri uri = Uri.parse(personListItemDTO.getPictureUrl().replace("https", "http"));
        personItemView.setPictureUri(uri);
    }

    @Override
    public void toggleScheduleStatus() {
        IInteractorAsyncOperationListener<Void> interactorAsyncOperationListener = new InteractorAsyncOperationListener<Void>() {
            @Override
            public void onError(String message) {
                view.showErrorMessage(message);
            }
        };

        scheduleablePresenter.toggleScheduledStatusForEvent(event, view, interactor, interactorAsyncOperationListener);
    }

    @Override
    public void showSpeakerProfile(int position) {
        PersonListItemDTO speaker = event.getSpeakers().get(position);
        wireframe.showSpeakerProfile(speaker.getId(), view);
    }

    @Override
    public void showFeedbackEdit() {
        wireframe.showFeedbackEditView(eventId, view);
    }

    @Override
    public void buildFeedbackListItem(FeedbackItemView feedbackItemView, int position) {
        FeedbackDTO feedback = feedbackList.get(position);
        feedbackItemView.setDate(feedback.getTimeAgo());
        feedbackItemView.setRate(feedback.getRate());
        feedbackItemView.setOwner(feedback.getOwner());
        feedbackItemView.setReview(feedback.getReview());
    }
}
