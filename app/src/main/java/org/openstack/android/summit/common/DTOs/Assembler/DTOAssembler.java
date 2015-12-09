package org.openstack.android.summit.common.DTOs.Assembler;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.entities.Company;
import org.openstack.android.summit.common.entities.SummitEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class DTOAssembler implements IDTOAssembler {
    private ModelMapper modelMapper = new ModelMapper();

    @Inject
    public DTOAssembler() {
/*        PropertyMap<SummitEvent, ScheduleItemDTO> orderMap = new PropertyMap<SummitEvent, ScheduleItemDTO>() {
            protected void configure() {
                String timeRange = getTimeRange(source);
                map().setDate(timeRange);
                String location = getLocation(source);
                map().setLocation(timeRange);
                map().setEventType(source.getEventType().getName());
                String sponsors = getSponsors(source);
                map().setSponsors(sponsors);
                String credentials = getCredentials(source);
                map().setSponsors(credentials);
            }
        };
        modelMapper.addMappings(orderMap);*/
    }

    @Override
    public <T,E> E createDTO(T source, Class<E> destinationType) {
        return modelMapper.map(source, destinationType);
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
            location = summitEvent.getVenueRoom().getVenue() + " - " + summitEvent.getVenueRoom().getName();
        }
        else if (summitEvent.getVenueRoom() != null){
            location = summitEvent.getVenue().getName();
        }
        return location;
    }

    private String getTimeRange(SummitEvent summitEvent) {
        DateFormat formatterFrom = new SimpleDateFormat("HH:mm");
        formatterFrom.setTimeZone(TimeZone.getTimeZone(summitEvent.getSummit().getTimeZone()));

        DateFormat formatterTo = new SimpleDateFormat("HH:mm");
        formatterTo.setTimeZone(TimeZone.getTimeZone(summitEvent.getSummit().getTimeZone()));

        String timeRange = String.format("%1 - %2", formatterFrom.format(summitEvent.getStart()), formatterTo.format(summitEvent.getEnd()));

        return timeRange;
    }

    private String getCredentials(SummitEvent summitEvent) {
        String credentials = "";

        if (summitEvent.getSummitTypes().size() > 0) {
            String separator = "";
            for(Company company: summitEvent.getSponsors()) {
                credentials += separator + company.getName();
                separator = ", ";
            }
        }

        return credentials;
    }
}
