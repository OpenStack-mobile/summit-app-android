package org.openstack.android.summit.common.data_access.DTOs.assembler;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openstack.android.summit.common.DTOs.Assembler.DTOAssembler;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.entities.Company;
import org.openstack.android.summit.common.entities.EventType;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.SummitType;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.entities.VenueRoom;
import org.robolectric.RobolectricTestRunner;

import java.util.Date;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
@RunWith(RobolectricTestRunner.class)
public class DTOAssemblerTests {
    @Test
    public void createDTO_summit2SummitDTO_createCorrectDTOInstance() {
        // Arrange
        DTOAssembler dtoAssembler = new DTOAssembler();
        Summit summit = new Summit();
        summit.setName("Tokio");
        summit.setStartDate(new Date(1447825312));
        summit.setEndDate(new Date(1447855312));
        summit.setTimeZone("CST");

        // Act
        SummitDTO summitDTO = dtoAssembler.createDTO(summit, SummitDTO.class);

        // Assert
        Assert.assertEquals(summitDTO.getName(), summit.getName());
        Assert.assertEquals(summitDTO.getStartDate().getTime(), summit.getStartDate().getTime());
        Assert.assertEquals(summitDTO.getEndDate().getTime(), summit.getEndDate().getTime());
        Assert.assertEquals(summitDTO.getTimeZone(), summit.getTimeZone());
    }

    @Test
    public void createDTO_summitEvent2SummitScheduleItemDTO_createCorrectDTOInstance() {
        // Arrange
        DTOAssembler dtoAssembler = new DTOAssembler();
        Summit summit = new Summit();
        summit.setTimeZone("Asia/Tokyo");

        Company company1 = new Company();
        company1.setName("company 1");

        Company company2 = new Company();
        company2.setName("company 2");

        EventType eventType = new EventType();
        eventType.setName("Keynotes");

        Venue venue = new Venue();
        venue.setId(2);
        venue.setName("Grand Prince International Convention Center & Hotels");

        VenueRoom venueRoom = new VenueRoom();
        venueRoom.setId(1);
        venueRoom.setName("PAMIR Building 3, Level 1F");
        venueRoom.setVenue(venue);

        SummitType summitType1 = new SummitType();
        summitType1.setId(1);
        summitType1.setName("Main Summit");

        SummitType summitType2 = new SummitType();
        summitType2.setId(2);
        summitType2.setName("Design Summit");

        SummitEvent summitEvent = new SummitEvent();
        summitEvent.setId(4);
        summitEvent.setName("Registration Check-In");
        summitEvent.setStart(new Date(1441137600L*1000));
        summitEvent.setEnd(new Date(1441141200L*1000));
        summitEvent.setSummit(summit);
        summitEvent.getSponsors().add(company1);
        summitEvent.getSponsors().add(company2);
        summitEvent.setEventType(eventType);
        summitEvent.setVenueRoom(venueRoom);
        summitEvent.getSummitTypes().add(summitType1);
        summitEvent.getSummitTypes().add(summitType2);

        // Act
        ScheduleItemDTO scheduleItemDTO = dtoAssembler.createDTO(summitEvent, ScheduleItemDTO.class);

        // Assert
        Assert.assertEquals(summitEvent.getId(), scheduleItemDTO.getId());
        Assert.assertEquals(summitEvent.getName(), scheduleItemDTO.getName());
        Assert.assertEquals("05:00 AM / 06:00 AM", scheduleItemDTO.getTime());
        Assert.assertEquals("Wednesday 02 September 05:00 AM / 06:00 AM", scheduleItemDTO.getDateTime());
        Assert.assertEquals("Grand Prince International Convention Center & Hotels - PAMIR Building 3, Level 1F", scheduleItemDTO.getLocation());
        Assert.assertEquals("Sponsored by company 1, company 2", scheduleItemDTO.getSponsors());
        Assert.assertEquals("Main Summit, Design Summit", scheduleItemDTO.getCredentials());
    }
}
