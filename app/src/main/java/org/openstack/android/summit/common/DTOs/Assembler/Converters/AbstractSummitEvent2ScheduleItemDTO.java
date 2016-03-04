package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.modelmapper.AbstractConverter;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.EventDetailDTO;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.entities.Company;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.SummitType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import io.realm.SummitEventRealmProxy;

/**
 * Created by Claudio Redi on 12/28/2015.
 */
public class AbstractSummitEvent2ScheduleItemDTO<S extends SummitEvent, T extends ScheduleItemDTO> extends AbstractConverter<S, T> {
    protected void convertInternal(S source, ScheduleItemDTO scheduleItemDTO) {
        scheduleItemDTO.setId(source.getId());
        scheduleItemDTO.setName(source.getName());
        String time = getTime(source);
        scheduleItemDTO.setTime(time);
        String dateTime = getDateTime(source);
        scheduleItemDTO.setDateTime(dateTime);
        String location = getLocation(source);
        scheduleItemDTO.setLocation(location);
        scheduleItemDTO.setRoom(source.getVenueRoom() != null ?source.getVenueRoom().getName() : null);
        scheduleItemDTO.setEventType(source.getEventType().getName());
        String sponsors = getSponsors(source);
        scheduleItemDTO.setSponsors(sponsors);
        String credentials = getCredentials(source);
        scheduleItemDTO.setCredentials(credentials);
        scheduleItemDTO.setTrack(source.getPresentation() != null ? source.getPresentation().getTrack().getName() : null);

        if (source.getPresentation() != null && source.getPresentation().getTrack().getTrackGroups().size() > 0) {
            String color = source.getPresentation().getTrack().getTrackGroups().get(0).getColor();
            scheduleItemDTO.setColor(color);
        }
    }

    private String getSponsors(S summitEvent) {
        String sponsors = "";

        if (summitEvent.getSponsors().size() > 0) {
            sponsors = "Sponsored by ";
            String separator = "";
            for(Company company: summitEvent.getSponsors()) {
                sponsors += separator + company.getName();
                separator = ", ";
            }
        }

        return sponsors;
    }

    private String getLocation(S summitEvent) {
        String location = "";
        if (summitEvent.getVenueRoom() != null) {
            location = summitEvent.getVenueRoom().getVenue().getName() + " - " + summitEvent.getVenueRoom().getName();
        }
        else if (summitEvent.getVenue() != null){
            location = summitEvent.getVenue().getName();
        }
        return location;
    }

    private String getDateTime(S summitEvent) {
        DateFormat formatterFrom = new SimpleDateFormat("EEEE dd MMMM hh:mm a");
        formatterFrom.setTimeZone(TimeZone.getTimeZone(summitEvent.getSummit().getTimeZone()));

        DateFormat formatterTo = new SimpleDateFormat("hh:mm a");
        formatterTo.setTimeZone(TimeZone.getTimeZone(summitEvent.getSummit().getTimeZone()));

        String timeRange = String.format("%s / %s", formatterFrom.format(summitEvent.getStart()), formatterTo.format(summitEvent.getEnd()));

        return timeRange;
    }

    private String getTime(S summitEvent) {
        DateFormat formatterFrom = new SimpleDateFormat("hh:mm a");
        formatterFrom.setTimeZone(TimeZone.getTimeZone(summitEvent.getSummit().getTimeZone()));

        DateFormat formatterTo = new SimpleDateFormat("hh:mm a");
        formatterTo.setTimeZone(TimeZone.getTimeZone(summitEvent.getSummit().getTimeZone()));

        String timeRange = String.format("%s / %s", formatterFrom.format(summitEvent.getStart()), formatterTo.format(summitEvent.getEnd()));

        return timeRange.toLowerCase();
    }

    private String getCredentials(S summitEvent) {
        String credentials = "";

        if (summitEvent.getSummitTypes().size() > 0) {
            String separator = "";
            for(SummitType summitType: summitEvent.getSummitTypes()) {
                credentials += separator + summitType.getName();
                separator = ", ";
            }
        }

        return credentials;
    }

    @Override
    protected T convert(S source) {
        ScheduleItemDTO scheduleItemDTO = new ScheduleItemDTO();
        try {
            convertInternal(source, scheduleItemDTO);
        }
        catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            throw e;
        }
        return (T)scheduleItemDTO;
    }
}
