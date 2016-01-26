package org.openstack.android.summit.modules.event_detail.user_interface;

import android.net.Uri;
import android.os.Bundle;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.EventDetailDTO;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.business_logic.IInteractorAsyncOperationListener;
import org.openstack.android.summit.common.business_logic.InteractorAsyncOperationListener;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.common.user_interface.IScheduleablePresenter;
import org.openstack.android.summit.common.user_interface.PersonItemView;
import org.openstack.android.summit.modules.event_detail.IEventDetailWireframe;
import org.openstack.android.summit.modules.event_detail.business_logic.IEventDetailInteractor;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/21/2016.
 */
public class EventDetailPresenter extends BasePresenter<IEventDetailFragment, IEventDetailInteractor, IEventDetailWireframe> implements IEventDetailPresenter {

    private int eventId;
    private EventDetailDTO event;
    private IScheduleablePresenter scheduleablePresenter;

    @Inject
    public EventDetailPresenter(IEventDetailInteractor interactor, IEventDetailWireframe wireframe, IScheduleablePresenter scheduleablePresenter) {
        super(interactor, wireframe);
        this.scheduleablePresenter = scheduleablePresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            eventId = savedInstanceState.getInt(Constants.NAVIGATION_PARAMETER_EVENT_ID);
        }
        else {
            eventId = wireframe.getParameter(Constants.NAVIGATION_PARAMETER_EVENT_ID, Integer.class);
        }

        event = interactor.getEventDetail(eventId);
        view.setName(event.getName());
        view.setTrack(event.getTrack());
        view.setDate(event.getDateTime());
        view.setLocation(event.getLocation());
        view.setDescription(event.getEventDescription());
        view.setCredentials(event.getCredentials());
        view.setLevel(event.getLevel());
        view.setSponsors(event.getSponsors());
        view.setSpeakers(event.getSpeakers());
        view.setScheduled(interactor.isEventScheduledByLoggedMember(eventId));
        view.setIsScheduledStatusVisible(interactor.isMemberLoggedIn());
        view.setTags(event.getTags());
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
}
