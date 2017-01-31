package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.EventDetailDTO;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.PresentationVideo;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.Tag;

/**
 * Created by Claudio Redi on 1/21/2016.
 */
public class AbstractSummitEvent2EventDetailDTO<E extends SummitEvent, S extends PresentationSpeaker, V extends PresentationVideo> extends AbstractSummitEvent2ScheduleItemDTO<E, EventDetailDTO> {

    protected AbstractPresentationSpeaker2PersonListIemDTO<S, PersonListItemDTO> presentationSpeaker2PersonListIemDTO;
    protected AbstractPresentationVideo2VideoDTO<V> video2VideoDTO;

    @Override
    protected EventDetailDTO convert(E source) {
        EventDetailDTO eventDetailDTO = new EventDetailDTO();

        try {
            convertInternal(source, eventDetailDTO);

            if (source.getVenueRoom() != null) {
                if(source.getVenueRoom().getVenue() != null)
                    eventDetailDTO.setVenueId(source.getVenueRoom().getVenue().getId());
                eventDetailDTO.setVenueRoomId(source.getVenueRoom().getId());
                if (source.getVenueRoom().getFloor() != null) {
                    eventDetailDTO.setVenueFloorId(source.getVenueRoom().getFloor().getId());
                }
            } else if (source.getVenue() != null) {
                eventDetailDTO.setVenueId(source.getVenue().getId());
            }

            eventDetailDTO.setAllowFeedback(source.getAllowFeedback());
            eventDetailDTO.setStarted(isStarted(source));
            eventDetailDTO.setTags(getTags(source));
            eventDetailDTO.setEventDescription(source.getDescription());
            eventDetailDTO.setAverageRate(source.getAverageRate());
            eventDetailDTO.setHeadCount(source.getHeadCount());
            eventDetailDTO.setRsvpLink(source.getRsvpLink());
            Summit summit = source.getSummit();

            if (summit != null) {
                String templateUrl = summit.getScheduleEventDetailUrl();
                String eventUrl    = templateUrl
                        .replace(":event_id", Integer.toString(source.getId()))
                        .replace(":event_title", eventDetailDTO.getSlug());

                eventDetailDTO.setEventUrl(eventUrl);
            }

            if (source.getPresentation() != null) {
                eventDetailDTO.setTrack(
                        source.getTrack() != null ? source.getTrack().getName() : ""
                );
                eventDetailDTO.setLevel(source.getPresentation().getLevel() + " Level");

                PersonListItemDTO speakerListItemDTO;
                for (PresentationSpeaker presentationSpeaker : source.getPresentation().getSpeakers()) {
                    if (presentationSpeaker.getFullName() != null && !presentationSpeaker.getFullName().isEmpty()) {
                        speakerListItemDTO = presentationSpeaker2PersonListIemDTO.convert((S) presentationSpeaker);
                        eventDetailDTO.getSpeakers().add(speakerListItemDTO);
                    }
                }

                if (source.getPresentation().getModerator() != null) {
                    speakerListItemDTO = presentationSpeaker2PersonListIemDTO.convert((S) source.getPresentation().getModerator());
                    eventDetailDTO.setModerator(speakerListItemDTO);
                }

                if (source.getPresentation().getVideos().size() > 0) {
                    PresentationVideo video = source.getPresentation().getVideos().first();
                    eventDetailDTO.setVideo(video2VideoDTO.convert((V) video));
                }
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            throw e;
        }

        return eventDetailDTO;
    }

    private String getTags(E source) {
        String tags = "";
        String separator = "";

        for (Tag tag : source.getTags()) {
            tags += separator + tag.getTag();
            separator = ", ";
        }

        return tags;
    }

    private Boolean isStarted(E source){
        return source.getStart().getTime() <= System.currentTimeMillis();
    }
}