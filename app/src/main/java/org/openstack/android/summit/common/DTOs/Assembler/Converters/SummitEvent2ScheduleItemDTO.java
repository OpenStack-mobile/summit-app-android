package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import org.modelmapper.AbstractConverter;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.entities.Company;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.SummitType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by Claudio Redi on 12/19/2015.
 */
public class SummitEvent2ScheduleItemDTO extends AbstractConverter<SummitEvent, ScheduleItemDTO> {

    @Override
    protected ScheduleItemDTO convert(SummitEvent source) {
        ScheduleItemDTO scheduleItemDTO = new ScheduleItemDTO();

        scheduleItemDTO.setId(source.getId());
        scheduleItemDTO.setName(source.getName());
        String time = getTime(source);
        scheduleItemDTO.setTime(time);
        String dateTime = getDateTime(source);
        scheduleItemDTO.setDateTime(dateTime);
        String location = getLocation(source);
        scheduleItemDTO.setLocation(location);
        scheduleItemDTO.setEventType(source.getEventType().getName());
        String sponsors = getSponsors(source);
        scheduleItemDTO.setSponsors(sponsors);
        String credentials = getCredentials(source);
        scheduleItemDTO.setCredentials(credentials);
        return scheduleItemDTO;
    }

    private String getSponsors(SummitEvent summitEvent) {
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

    private String getLocation(SummitEvent summitEvent) {
        String location = "";
        if (summitEvent.getVenueRoom() != null) {
            location = summitEvent.getVenueRoom().getVenue().getName() + " - " + summitEvent.getVenueRoom().getName();
        }
        else if (summitEvent.getVenue() != null){
            location = summitEvent.getVenue().getName();
        }
        return location;
    }

    private String getDateTime(SummitEvent summitEvent) {
        DateFormat formatterFrom = new SimpleDateFormat("EEEE dd MMMM hh:mm a");
        formatterFrom.setTimeZone(TimeZone.getTimeZone(summitEvent.getSummit().getTimeZone()));

        DateFormat formatterTo = new SimpleDateFormat("hh:mm a");
        formatterTo.setTimeZone(TimeZone.getTimeZone(summitEvent.getSummit().getTimeZone()));

        String timeRange = String.format("%s / %s", formatterFrom.format(summitEvent.getStart()), formatterTo.format(summitEvent.getEnd()));

        return timeRange;
    }

    private String getTime(SummitEvent summitEvent) {
        DateFormat formatterFrom = new SimpleDateFormat("hh:mm a");
        formatterFrom.setTimeZone(TimeZone.getTimeZone(summitEvent.getSummit().getTimeZone()));

        DateFormat formatterTo = new SimpleDateFormat("HH:mm a");
        formatterTo.setTimeZone(TimeZone.getTimeZone(summitEvent.getSummit().getTimeZone()));

        String timeRange = String.format("%s / %s", formatterFrom.format(summitEvent.getStart()), formatterTo.format(summitEvent.getEnd()));

        return timeRange;
    }

    private String getCredentials(SummitEvent summitEvent) {
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
}
