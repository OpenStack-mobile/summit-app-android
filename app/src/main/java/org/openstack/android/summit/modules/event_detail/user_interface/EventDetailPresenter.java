package org.openstack.android.summit.modules.event_detail.user_interface;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.R;
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

    private Integer eventId;
    private EventDetailDTO event;
    private IScheduleablePresenter scheduleablePresenter;
    private FeedbackDTO myFeedbackForEvent;
    private List<FeedbackDTO> feedbackList = new ArrayList<>();
    private boolean loadingFeedback;
    private boolean loadedAllFeedback;
    private int feedbackPage           = 1;
    private int feedbackObjectsPerPage = 5;
    private boolean loadingFeedbackAverage;

    @Inject
    public EventDetailPresenter(IEventDetailInteractor interactor, IEventDetailWireframe wireframe, IScheduleablePresenter scheduleablePresenter) {
        super(interactor, wireframe);
        this.scheduleablePresenter = scheduleablePresenter;
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

        if (event == null) {
            view.setName("");
            view.setTrack("");
            view.setDate("");
            view.setDescription("");
            view.setCredentials("");
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
        view.setDescription(event.getEventDescription());
        view.setCredentials(event.getCredentials());
        view.setLevel(event.getLevel());
        view.setSponsors(event.getSponsors());
        view.setSpeakers(event.getModeratorAndSpeakers());
        view.setScheduled(interactor.isEventScheduledByLoggedMember(eventId));
        view.setIsScheduledStatusVisible(interactor.isMemberLoggedAndConfirmedAttendee());
        view.setAllowNewFeedback(getAllowNewFeedback());
        view.setTags(event.getTags());
        view.setAllowRsvp(event.getAllowRsvp() && interactor.isMemberLoggedAndConfirmedAttendee());
        view.hasMyFeedback(myFeedbackForEvent != null);

        if (event.getVideo() != null && event.getVideo().getYouTubeId() != null) {
            view.loadVideo(event.getVideo());
        }

        view.showLocation(interactor.shouldShowVenues());
        if (interactor.shouldShowVenues()) {
            view.setLocation(event.getLocation());
        }

        if (myFeedbackForEvent != null) {
            view.setMyFeedbackRate(myFeedbackForEvent.getRate());
            view.setMyFeedbackReview(myFeedbackForEvent.getReview());
            view.setMyFeedbackDate(myFeedbackForEvent.getTimeAgo());
            view.setMyFeedbackOwner(myFeedbackForEvent.getOwner());
        }

        view.setAverageRate(0); // TODO: we should implement a hide
        if (event.isStarted() && event.getAllowFeedback()) {
            loadFeedback();
            loadAverageFeedback();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        view.showAddToMyCalendar(!isEventInCalendar(view.getApplicationContext()));
    }

    private boolean isEventInCalendar(Context context) {
        Cursor cursor = null;
        try {
            if(event == null) return true;
            int permissionCheck = ContextCompat.checkSelfPermission(view.getApplicationContext(), Manifest.permission.READ_CALENDAR);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                return true;
            }

            cursor = CalendarContract.Instances.query(context.getContentResolver(), new String[]{
                            CalendarContract.Instances._ID,
                            CalendarContract.Instances.BEGIN,
                            CalendarContract.Instances.END,
                            CalendarContract.Instances.EVENT_ID},
                    event.getStartDate().getMillis(),
                    event.getEndDate().getMillis(),
                    String.format("\"%s\"", event.getName()));

            if (cursor != null && cursor.moveToFirst()) {
                return true;
            }
            return false;
        } finally {
            if (cursor != null)
                cursor.close();
        }
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
                List<FeedbackDTO> feedbackPageWithoutMe = new ArrayList<FeedbackDTO>();
                for (FeedbackDTO feedbackDTO : data) {
                    // TODO: if there are more than one owners with the same name, this could potencially fail
                    if (myFeedbackForEvent == null || (feedbackDTO.getOwner() != null && !feedbackDTO.getOwner().equals(myFeedbackForEvent.getOwner()))) {
                        feedbackPageWithoutMe.add(feedbackDTO);
                    }
                }

                feedbackList.addAll(feedbackPageWithoutMe);
                view.setOtherPeopleFeedback(feedbackList);
                //self.viewController.hasAnyFeedback = self.feedbackList.count > 0
                feedbackPage++;
                loadedAllFeedback = data.size() < feedbackObjectsPerPage;
                view.toggleLoadMore(!loadedAllFeedback);
                showFeedbackInfoIfFinishedLoading();
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                loadingFeedback = false;
                view.showFeedbackErrorMessage(message);
                showFeedbackInfoIfFinishedLoading();
            }
        };

        interactor.getFeedbackForEvent(eventId, feedbackPage, feedbackObjectsPerPage, interactorAsyncOperationListener);
    }

    public void loadAverageFeedback() {
        loadingFeedbackAverage = true;
        view.showFeedbackActivityIndicator();

        IInteractorAsyncOperationListener<Double> interactorAsyncOperationListener = new InteractorAsyncOperationListener<Double>() {
            @Override
            public void onSucceedWithData(Double data) {
                super.onSucceedWithData(data);
                view.setAverageRate((int) Math.round(event.getAverageRate()));
                loadingFeedbackAverage = false;
                showFeedbackInfoIfFinishedLoading();
            }

            @Override
            public void onError(String message) {
                super.onError(message);
                loadingFeedback = false;
                view.showFeedbackErrorMessage(message);
                loadingFeedbackAverage = false;
                showFeedbackInfoIfFinishedLoading();
            }
        };

        interactor.getAverageFeedbackForEvent(eventId, interactorAsyncOperationListener);
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
    public void showEventRsvpView() {
        this.wireframe.presentEventRsvpView(event.getRsvpLink(), view);
    }

    @Override
    public Intent createShareIntent() {
        if (this.event == null) return null;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, String.format(
                view.getResources().getString(R.string.share_event_text),
                this.event.getEventUrl()
                )
        );
        return shareIntent;
    }

    @Override
    public void addToCalendar() {
        try {
            Intent intent = new Intent(Intent.ACTION_EDIT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.Events.CALENDAR_ID, event.getId())
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.getStartDate().getMillis())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event.getEndDate().getMillis())
                    .putExtra(CalendarContract.Events.TITLE, event.getName())
                    .putExtra(CalendarContract.Events.DESCRIPTION, android.text.Html.fromHtml(event.getEventDescription()).toString())
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, event.getLocationAddress())
                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

            view.startActivity(intent);
        }
        catch(ActivityNotFoundException ex1){
            view.showErrorMessage(view.getResources().getString(R.string.missing_activity_error));
            Crashlytics.logException(ex1);
            Log.w(Constants.LOG_TAG, ex1.getMessage());
        }
        catch(Exception ex){
            Crashlytics.logException(ex);
            Log.w(Constants.LOG_TAG, ex.getMessage());
        }
    }

    private boolean getAllowNewFeedback() {
        return event.getAllowFeedback() && event.isStarted() && interactor.isMemberLogged() &&
                myFeedbackForEvent == null;
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
        feedbackItemView.setOwner(feedback.getOwner());
        feedbackItemView.setReview(feedback.getReview());
    }
}
