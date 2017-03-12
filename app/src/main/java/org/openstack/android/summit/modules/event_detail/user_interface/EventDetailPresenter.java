package org.openstack.android.summit.modules.event_detail.user_interface;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.EventDetailDTO;
import org.openstack.android.summit.common.DTOs.FeedbackDTO;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.user_interface.BaseScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.FeedbackItemView;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.PersonItemView;
import org.openstack.android.summit.common.user_interface.ShareIntentBuilder;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.event_detail.business_logic.IEventDetailInteractor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Claudio Redi on 1/21/2016.
 */
public class EventDetailPresenter
        extends BaseScheduleablePresenter<IEventDetailView, IEventDetailInteractor, IEventDetailWireframe>
        implements IEventDetailPresenter {

    private Integer eventId;
    private EventDetailDTO event;
    private FeedbackDTO myFeedbackForEvent;
    private List<FeedbackDTO> feedbackList = new ArrayList<>();
    private boolean loadingFeedback;
    private boolean loadedAllFeedback;
    private              int feedbackPage           = 1;
    private static final int feedbackObjectsPerPage = 25;
    private boolean loadingFeedbackAverage;

    @Inject
    public EventDetailPresenter
    (
            IEventDetailInteractor interactor,
            IEventDetailWireframe wireframe,
            IScheduleablePresenter scheduleablePresenter
    )
    {
        super(interactor, wireframe, scheduleablePresenter);
    }

    @Override
    protected ScheduleItemDTO getCurrentItem(int position) {
        return this.event;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventId = (savedInstanceState != null) ?
                savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_EVENT_ID,0) :
                wireframe.getParameter(Constants.NAVIGATION_PARAMETER_EVENT_ID, Integer.class);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        updateUI();
    }

    @Override
    public void updateUI(){

        event = interactor.getEventDetail(eventId != null ? eventId : 0);
        event.setChangeStatusListener(item -> updateContextMenuOptions());

        if (event == null) {
            view.setName("");
            view.setTrack("");
            view.setDate("");
            view.setDescription("");
            view.setLevel("");
            view.setSponsors("");
            view.setTags("");
            view.setLocation("");
            view.showInfoMessage("Event not found!");
            return;
        }

        loadedAllFeedback  = false;
        myFeedbackForEvent = interactor.getMyFeedbackForEvent(eventId);

        view.setName(event.getName());
        view.setTrack(event.getTrack());
        view.setDate(event.getDateTime());
        view.setTime(event.getTime());
        view.setDescription(event.getEventDescription());
        view.setLevel(event.getLevel());
        view.setSponsors(event.getSponsors());
        view.setSpeakers(event.getModeratorAndSpeakers());
        view.setTags(event.getTags());
        view.hasMyFeedback(myFeedbackForEvent != null);

        boolean hasVideo = false;
        if (event.getVideo() != null && event.getVideo().getYouTubeId() != null) {
            view.loadVideo(event.getVideo());
            hasVideo = true;
        }

        if (interactor.shouldShowVenues()) {
            view.setLocation(event.getLocation());
        }

        if (myFeedbackForEvent != null) {
            view.setMyFeedbackRate(myFeedbackForEvent.getRate());
            view.setMyFeedbackReview(myFeedbackForEvent.getReview());
            view.setMyFeedbackDate(myFeedbackForEvent.getTimeAgo());
        }

        view.setAverageRate(0);
        if (event.isStarted() && event.getAllowFeedback()) {
            loadFeedback();
            loadAverageFeedback();
        }

        if(event.isToRecord() && !hasVideo){
            view.showToRecord(true);
        }

        if(event.getAttachmentUrl() != null && !event.getAttachmentUrl().isEmpty()){
            view.showAttachment(true, event.isPresentation());
        }
    }

    @Override
    public void updateContextMenuOptions(){
        view.showGoingMenuAction(false);
        view.showNotGoingMenuAction(false);
        view.showAddFavoriteMenuAction(false);
        view.showRemoveFavoriteMenuAction(false);
        view.showRateMenuAction(false);
        view.showRSVPMenuAction(false);
        if(this.interactor.isMemberLoggedIn()){
            view.showRateMenuAction(myFeedbackForEvent == null && this.event.getAllowFeedback() && event.isStarted());
            view.showAddFavoriteMenuAction(!this.event.getFavorite());
            view.showRemoveFavoriteMenuAction(this.event.getFavorite());

            if(this.interactor.isMemberLoggedInAndConfirmedAttendee()){
                if(this.event.getRsvpLink() != null &&  !this.event.getRsvpLink().isEmpty())
                    view.showRSVPMenuAction(true);
                else{
                    view.showNotGoingMenuAction(this.event.getScheduled());
                    view.showGoingMenuAction(!this.event.getScheduled());
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void loadFeedback() {
        if (loadingFeedback || loadedAllFeedback) {
            return;
        }

        loadingFeedback = true;
        view.showFeedbackActivityIndicator();

        interactor
                .getFeedbackForEvent(eventId, feedbackPage, feedbackObjectsPerPage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ( data ) -> {
                            loadingFeedback = false;
                            List<FeedbackDTO> feedbackPageWithoutMe = new ArrayList<>();
                            for (FeedbackDTO feedbackDTO : data) {
                                // TODO: if there are more than one owners with the same name, this could potencially fail
                                if (myFeedbackForEvent == null ||
                                        (feedbackDTO.getOwner() != null &&
                                                !feedbackDTO.getOwner().equals(myFeedbackForEvent.getOwner()))) {
                                    feedbackPageWithoutMe.add(feedbackDTO);
                                }
                            }

                            feedbackList.addAll(feedbackPageWithoutMe);
                            view.setOtherPeopleFeedback(feedbackList);
                            feedbackPage++;
                            loadedAllFeedback = data.size() < feedbackObjectsPerPage;
                            view.toggleLoadMore(!loadedAllFeedback);
                            int reviewCount = feedbackList.size();
                            if(myFeedbackForEvent != null) ++reviewCount;
                            view.setReviewCount(reviewCount);
                            showFeedbackInfoIfFinishedLoading();
                        },
                        (ex) -> {
                            loadingFeedback = false;
                            view.showFeedbackErrorMessage(ex.getMessage());
                            showFeedbackInfoIfFinishedLoading();
                        }
                );

    }

    public void loadAverageFeedback() {
        loadingFeedbackAverage = true;
        view.showFeedbackActivityIndicator();

        interactor
                .getAverageFeedbackForEvent(eventId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ( avg ) -> {
                            view.setAverageRate(avg);
                            loadingFeedbackAverage = false;
                            showFeedbackInfoIfFinishedLoading();
                        },
                        (ex) -> {
                            loadingFeedback = false;
                            view.showFeedbackErrorMessage(ex.getMessage());
                            loadingFeedbackAverage = false;
                            showFeedbackInfoIfFinishedLoading();
                        }
                );
    }

    private void showFeedbackInfoIfFinishedLoading() {
        if (!loadingFeedback && !loadingFeedbackAverage) {
            view.showFeedbackContainer();
            view.hideFeedbackActivityIndicator();
        }
    }

    @Override
    public void showVenueDetail() {
        if (event.getVenueRoomId() > 0 && interactor.shouldShowVenues()) {
            wireframe.showEventLocationDetailView(event.getVenueRoomId(), view);
            return;
        }
        wireframe.showEventVenueDetailView(event.getVenueId(), view);
    }

    @Override
    public void showEventsByLevel() {
        wireframe.presentLevelScheduleView(event.getLevel(), view);
    }

    @Override
    public void openAttachment() {

        String fileUrl = this.event.getAttachmentUrl();
        if(fileUrl == null || fileUrl.isEmpty()) return;

        MimeTypeMap mime                = MimeTypeMap.getSingleton();
        ContentResolver contentResolver = view.getContentResolver();
        Intent newIntent                = new Intent(Intent.ACTION_VIEW);
        String mimeType                 = mime.getMimeTypeFromExtension(contentResolver.getType(Uri.parse(fileUrl)));

        newIntent.setDataAndType(Uri.parse(fileUrl),mimeType);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            view.startActivity(newIntent);
        } catch (Exception ex) {
            Log.e(Constants.LOG_TAG, ex.getMessage());
        }
    }

    @Override
    public Intent createShareIntent() {
        if (this.event == null) return null;
        return ShareIntentBuilder.build(this.event.getEventUrl());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(eventId != null)
            outState.putInt(Constants.NAVIGATION_PARAMETER_EVENT_ID, eventId);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            eventId = savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_EVENT_ID , 0);
        }
    }

    @Override
    public void buildSpeakerListItem(PersonItemView personItemView, int position) {
        PersonListItemDTO personListItemDTO = event.getModeratorAndSpeakers().get(position);
        personItemView.setName(personListItemDTO.getName());
        personItemView.setTitle(personListItemDTO.getTitle());
        personItemView.setIsModerator(
                event.getModerator() != null && personListItemDTO.getId() == event.getModerator().getId()
        );
        Uri uri = Uri.parse(personListItemDTO.getPictureUrl().replace("https", "http"));
        personItemView.setPictureUri(uri);
    }

    @Override
    public void toggleScheduleStatus() {
        _toggleScheduleStatus(event, 0);
    }

    @Override
    public void toggleFavoriteStatus() {
        _toggleFavoriteStatus(event, 0);
    }

    @Override
    public void toggleRSVPStatus() {
        _toggleRSVPStatus(event, 0);
    }

    @Override
    public void showSpeakerProfile(int position) {
        PersonListItemDTO speaker = event.getModeratorAndSpeakers().get(position);
        wireframe.showSpeakerProfile(speaker.getId(), view);
    }

    @Override
    public void showFeedbackEdit(int rate) {
        wireframe.showFeedbackEditView(eventId, rate, view);
    }

    @Override
    public void buildFeedbackListItem(FeedbackItemView feedbackItemView, int position) {
        FeedbackDTO feedback = feedbackList.get(position);
        feedbackItemView.setDate(feedback.getTimeAgo());
        feedbackItemView.setRate(feedback.getRate());
        feedbackItemView.setReview(feedback.getReview());
    }
}
