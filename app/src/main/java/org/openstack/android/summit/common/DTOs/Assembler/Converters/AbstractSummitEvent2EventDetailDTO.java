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
import org.openstack.android.summit.common.utils.LocalDateFormat;

import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Claudio Redi on 1/21/2016.
 */
public class AbstractSummitEvent2EventDetailDTO<E extends SummitEvent, S extends PresentationSpeaker, V extends PresentationVideo>
        extends AbstractSummitEvent2ScheduleItemDTO<E, EventDetailDTO> {

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

            eventDetailDTO.setTags(getTags(source));
            eventDetailDTO.setEventDescription(source.getDescription());
            eventDetailDTO.setAverageRate(source.getAverageRate());
            eventDetailDTO.setHeadCount(source.getHeadCount());
            eventDetailDTO.setRsvpLink(source.getRsvpLink());

            if(source.getSummitEventWithFile() != null){
                eventDetailDTO.setAttachmentUrl(source.getSummitEventWithFile().getAttachment());
            }

            if (source.getPresentation() != null) {
                eventDetailDTO.setTrack(
                        source.getTrack() != null ? source.getTrack().getName() : ""
                );

                eventDetailDTO.setPresentation(true);
                eventDetailDTO.setLevel(source.getPresentation().getLevel());

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

                if(source.getPresentation().getSlides() != null && !source.getPresentation().getSlides().isEmpty()){
                    eventDetailDTO.setAttachmentUrl(source.getPresentation().getSlides().first().getLink());
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


    @Override
    protected String getDateTime(E summitEvent) {
        Summit summit = null;
        try {

            summit = summitEvent.getSummit();

            if(summit == null)
                throw new InvalidParameterException("missing summit on event id "+ summitEvent.getId());

            if(summit.getTimeZone() == null)
                throw new InvalidParameterException("summit timezone id is not set for summit id "+ summit.getId());

            TimeZone timeZone = TimeZone.getTimeZone(summit.getTimeZone());

            DateFormat formatterFrom = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            formatterFrom.setTimeZone(timeZone);

            return String.format(Locale.US, "%s", formatterFrom.format(summitEvent.getStart()));

        }
        catch (Exception ex){
            Log.w(Constants.LOG_TAG, ex);
            Crashlytics.logException(ex);
        }
        return "INVALID";
    }

    @Override
    protected String getTime(E summitEvent) {
        Summit summit = null;
        try {
            DateFormat formatterFrom = new LocalDateFormat("hh:mm a", Locale.US);
            summit                   = summitEvent.getSummit();

            if(summit == null)
                throw new InvalidParameterException("missing summit on event id "+ summitEvent.getId());

            if(summit.getTimeZone() == null)
                throw new InvalidParameterException("summit timezone id is not set for summit id "+ summit.getId());

            TimeZone timeZone = TimeZone.getTimeZone(summit.getTimeZone());
            formatterFrom.setTimeZone(timeZone);
            DateFormat formatterTo = new LocalDateFormat("hh:mm a", Locale.US);
            formatterTo.setTimeZone(timeZone);
            String timeRange = String.format(Locale.US, "%s - %s", formatterFrom.format(summitEvent.getStart()), formatterTo.format(summitEvent.getEnd()));
            return timeRange.toLowerCase();
        }
        catch (Exception ex){
            Log.w(Constants.LOG_TAG, ex);
            Crashlytics.logException(ex);
        }
        return "INVALID";
    }

}