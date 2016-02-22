package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;

import org.modelmapper.AbstractConverter;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.EventDetailDTO;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.Tag;

/**
 * Created by Claudio Redi on 1/21/2016.
 */
public class AbstractSummitEvent2EventDetailDTO<E extends SummitEvent, S extends PresentationSpeaker> extends AbstractSummitEvent2ScheduleItemDTO<E, EventDetailDTO> {

    protected AbstractPresentationSpeaker2PersonListIemDTO<S, PersonListItemDTO> presentationSpeaker2PersonListIemDTO;

    @Override
    protected EventDetailDTO convert(E source) {
        EventDetailDTO eventDetailDTO = new EventDetailDTO();

        try {
            convertInternal(source, eventDetailDTO);
            eventDetailDTO.setVenueId(source.getVenue() != null ? source.getVenue().getId() : 0);
            eventDetailDTO.setVenueRoomId(source.getVenueRoom() != null ? source.getVenueRoom().getId() : 0);
            eventDetailDTO.setAllowFeedback(source.getAllowFeedback());
            eventDetailDTO.setFinished(getFinished(source));
            eventDetailDTO.setTags(getTags(source));
            eventDetailDTO.setEventDescription(source.getEventDescription());
            eventDetailDTO.setAverageRate(source.getAverageRate());
            if (source.getPresentation() != null) {
                eventDetailDTO.setTrack(source.getPresentation().getTrack().getName());
                eventDetailDTO.setLevel(source.getPresentation().getLevel() + " Level");

                PersonListItemDTO speakerListItemDTO;
                for (PresentationSpeaker presentationSpeaker: source.getPresentation().getSpeakers()) {
                    speakerListItemDTO = presentationSpeaker2PersonListIemDTO.convert((S)presentationSpeaker);
                    eventDetailDTO.getSpeakers().add(speakerListItemDTO);
                }

                if (source.getPresentation().getModerator() != null) {
                    speakerListItemDTO = presentationSpeaker2PersonListIemDTO.convert((S)source.getPresentation().getModerator());
                    eventDetailDTO.setModerator(speakerListItemDTO);
                }
            }
        }
        catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            throw e;
        }

        return eventDetailDTO;
    }

    private String getTags(E source) {
        String tags = "";
        String separator = "";

        for(Tag tag: source.getTags()) {
            tags += separator + tag.getTag();
            separator = ", ";
        }

        return tags;
    }

    private Boolean getFinished(E source) {
        return source.getEnd().getTime() < System.currentTimeMillis();
    }
}