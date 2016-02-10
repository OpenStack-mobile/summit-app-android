package org.openstack.android.summit.common.data_access.DTOs.assembler;

import junit.framework.Assert;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.DTOs.Assembler.DTOAssembler;
import org.openstack.android.summit.common.DTOs.EventDetailDTO;
import org.openstack.android.summit.common.DTOs.FeedbackDTO;
import org.openstack.android.summit.common.DTOs.PersonListItemDTO;
import org.openstack.android.summit.common.DTOs.ScheduleItemDTO;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.data_access.MockSupport;
import org.openstack.android.summit.common.entities.Company;
import org.openstack.android.summit.common.entities.EventType;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Presentation;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.SummitType;
import org.openstack.android.summit.common.entities.Tag;
import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.entities.TrackGroup;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.entities.VenueRoom;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Date;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
@RunWith(RobolectricTestRunner.class)
public class DTOAssemblerTests {

    @Before
    public void setup() {
        OpenStackSummitApplication.context = RuntimeEnvironment.application;
    }

    @Test
    public void createDTO_summit2SummitDTO_createCorrectDTOInstance() {
        // Arrange
        DTOAssembler dtoAssembler = new DTOAssembler();
        Summit summit = new Summit();
        summit.setName("Tokio");
        summit.setStartDate(new Date(1447825312L*1000));
        summit.setEndDate(new Date(1447855312L*1000));
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

        TrackGroup trackGroup1 = new TrackGroup();
        trackGroup1.setId(11);
        trackGroup1.setColor("#564534");

        TrackGroup trackGroup2 = new TrackGroup();
        trackGroup2.setId(11);
        trackGroup2.setColor("#564534");

        Track track = new Track();
        track.setId(1);
        track.setName("test track");
        track.getTrackGroups().add(trackGroup1);
        track.getTrackGroups().add(trackGroup2);

        Presentation presentation = new Presentation();
        presentation.setId(2000);
        presentation.setTrack(track);

        SummitEvent summitEvent = new SummitEvent();
        summitEvent.setId(4);
        summitEvent.setName("Registration Check-In");
        summitEvent.setStart(new Date(1441137600L * 1000));
        summitEvent.setEnd(new Date(1441141200L * 1000));
        summitEvent.setSummit(summit);
        summitEvent.getSponsors().add(company1);
        summitEvent.getSponsors().add(company2);
        summitEvent.setEventType(eventType);
        summitEvent.setVenueRoom(venueRoom);
        summitEvent.getSummitTypes().add(summitType1);
        summitEvent.getSummitTypes().add(summitType2);
        summitEvent.setPresentation(presentation);

        // Act
        ScheduleItemDTO scheduleItemDTO = dtoAssembler.createDTO(summitEvent, ScheduleItemDTO.class);

        // Assert
        Assert.assertEquals(summitEvent.getId(), scheduleItemDTO.getId());
        Assert.assertEquals(summitEvent.getName(), scheduleItemDTO.getName());
        Assert.assertEquals("05:00 am / 06:00 am", scheduleItemDTO.getTime());
        Assert.assertEquals("Wednesday 02 September 05:00 AM / 06:00 AM", scheduleItemDTO.getDateTime());
        Assert.assertEquals("Grand Prince International Convention Center & Hotels - PAMIR Building 3, Level 1F", scheduleItemDTO.getLocation());
        Assert.assertEquals("Sponsored by company 1, company 2", scheduleItemDTO.getSponsors());
        Assert.assertEquals("Main Summit, Design Summit", scheduleItemDTO.getCredentials());
        Assert.assertEquals(track.getName(), scheduleItemDTO.getTrack());
        Assert.assertEquals(trackGroup1.getColor(), scheduleItemDTO.getColor());
    }

    @Test
    public void createDTO_presentationSpeaker2PersonListItemDTO_createCorrectDTOInstance() {
        // Arrange
        DTOAssembler dtoAssembler = new DTOAssembler();
        PresentationSpeaker presentationSpeaker = new PresentationSpeaker();
        presentationSpeaker.setId(1);
        presentationSpeaker.setFirstName("Aaron");
        presentationSpeaker.setLastName("Delp");
        presentationSpeaker.setTitle("Technical Solutions Director");
        presentationSpeaker.setPictureUrl("https://pictureurl.com/picture.png");

        // Act
        PersonListItemDTO personListItemDTO = dtoAssembler.createDTO(presentationSpeaker, PersonListItemDTO.class);

        // Assert
        Assert.assertEquals(personListItemDTO.getId(), presentationSpeaker.getId());
        Assert.assertEquals(personListItemDTO.getName(), presentationSpeaker.getFirstName() + " " + presentationSpeaker.getLastName());
        Assert.assertEquals(personListItemDTO.getTitle(), presentationSpeaker.getTitle());
        Assert.assertEquals(personListItemDTO.getPictureUrl(), presentationSpeaker.getPictureUrl());
        Assert.assertTrue(personListItemDTO.getIsSpeaker());
        Assert.assertFalse(personListItemDTO.getIsAttendee());
    }

    @Test
    public void createDTO_summitEvent2EventDetailDTO_createCorrectDTOInstance() {
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

        TrackGroup trackGroup = new TrackGroup();
        trackGroup.setId(11);
        trackGroup.setColor("#564534");

        Track track = new Track();
        track.setId(1);
        track.setName("test track");
        track.getTrackGroups().add(trackGroup);

        Tag tag1 = new Tag();
        tag1.setId(3);
        tag1.setTag("tag1");

        Tag tag2 = new Tag();
        tag2.setId(4);
        tag2.setTag("tag2");

        PresentationSpeaker presentationSpeaker1 = new PresentationSpeaker();
        presentationSpeaker1.setId(1);
        presentationSpeaker1.setFullName("Test Speaker");

        PresentationSpeaker presentationSpeaker2 = new PresentationSpeaker();
        presentationSpeaker2.setId(2);
        presentationSpeaker2.setFullName("Test Moderator");

        Presentation presentation = new Presentation();
        presentation.setId(2000);
        presentation.setTrack(track);
        presentation.getSpeakers().add(presentationSpeaker1);
        presentation.getSpeakers().add(presentationSpeaker2);
        presentation.setModerator(presentationSpeaker2);

        SummitEvent summitEvent = new SummitEvent();
        summitEvent.setId(4);
        summitEvent.setName("Registration Check-In");
        summitEvent.setStart(new Date(1441137600L * 1000));
        summitEvent.setEnd(new Date(1441141200L * 1000));
        summitEvent.setSummit(summit);
        summitEvent.getSponsors().add(company1);
        summitEvent.getSponsors().add(company2);
        summitEvent.setEventType(eventType);
        summitEvent.setVenueRoom(venueRoom);
        summitEvent.getSummitTypes().add(summitType1);
        summitEvent.getSummitTypes().add(summitType2);
        summitEvent.setPresentation(presentation);
        summitEvent.getTags().add(tag1);
        summitEvent.getTags().add(tag2);
        summitEvent.setAllowFeedback(false);

        // Act
        EventDetailDTO eventDetailDTO = dtoAssembler.createDTO(summitEvent, EventDetailDTO.class);

        // Assert
        Assert.assertEquals(summitEvent.getId(), eventDetailDTO.getId());
        Assert.assertEquals(summitEvent.getName(), eventDetailDTO.getName());
        Assert.assertEquals("05:00 am / 06:00 am", eventDetailDTO.getTime());
        Assert.assertEquals("Wednesday 02 September 05:00 AM / 06:00 AM", eventDetailDTO.getDateTime());
        Assert.assertEquals("Grand Prince International Convention Center & Hotels - PAMIR Building 3, Level 1F", eventDetailDTO.getLocation());
        Assert.assertEquals("Sponsored by company 1, company 2", eventDetailDTO.getSponsors());
        Assert.assertEquals("Main Summit, Design Summit", eventDetailDTO.getCredentials());
        Assert.assertEquals(track.getName(), eventDetailDTO.getTrack());
        Assert.assertEquals("tag1, tag2", eventDetailDTO.getTags());
        Assert.assertEquals(1, eventDetailDTO.getVenueRoomId());
        Assert.assertEquals(2, eventDetailDTO.getSpeakers().size());
        Assert.assertEquals(2, eventDetailDTO.getModerator().getId());
        Assert.assertTrue(eventDetailDTO.getFinished());
        Assert.assertFalse(eventDetailDTO.getAllowFeedback());
        Assert.assertEquals(trackGroup.getColor(), eventDetailDTO.getColor());
    }

    @Test
    public void createDTO_feedback2FeedbackDTO_createCorrectDTOInstance() {
        // Arrange
        DTOAssembler dtoAssembler = new DTOAssembler();
        SummitEvent summitEvent = new SummitEvent();
        summitEvent.setId(4);
        summitEvent.setName("Registration Check-In");

        SummitAttendee summitAttendee = new SummitAttendee();
        summitAttendee.setId(2);
        summitAttendee.setFullName("Claudio Redi");

        Feedback feedback = new Feedback();
        feedback.setId(1);
        feedback.setEvent(summitEvent);
        feedback.setOwner(summitAttendee);
        feedback.setDate(new Date(new Date().getTime() - 1000*60*1));
        feedback.setRate(4);

        // Act
        FeedbackDTO feedbackDTO = dtoAssembler.createDTO(feedback, FeedbackDTO.class);

        // Assert
        Assert.assertEquals(feedback.getId(), feedbackDTO.getId());
        Assert.assertEquals(feedback.getEvent().getId(), feedbackDTO.getEventId());
        Assert.assertEquals(feedback.getEvent().getName(), feedbackDTO.getEventName());
        Assert.assertEquals(feedback.getRate(), feedbackDTO.getRate());
        Assert.assertEquals(feedback.getReview(), feedbackDTO.getReview());
        Assert.assertEquals(feedback.getOwner().getFullName(), feedbackDTO.getOwner());
        Assert.assertEquals("1 min ago", feedbackDTO.getTimeAgo());
        Assert.assertEquals(feedback.getDate(), feedbackDTO.getDate());
    }

    @Test
    public void createDTO_feedbackFromOneDayAgo2FeedbackDTO_createCorrectTimeAgo() {
        // Arrange
        DTOAssembler dtoAssembler = new DTOAssembler();
        SummitEvent summitEvent = new SummitEvent();
        summitEvent.setId(4);
        summitEvent.setName("Registration Check-In");

        SummitAttendee summitAttendee = new SummitAttendee();
        summitAttendee.setId(2);
        summitAttendee.setFullName("Claudio Redi");

        Feedback feedback = new Feedback();
        feedback.setId(1);
        feedback.setEvent(summitEvent);
        feedback.setOwner(summitAttendee);
        feedback.setDate(new Date(new Date().getTime() - 1000*60*60*24));
        feedback.setRate(4);

        // Act
        FeedbackDTO feedbackDTO = dtoAssembler.createDTO(feedback, FeedbackDTO.class);

        // Assert
        Assert.assertEquals("yesterday", feedbackDTO.getTimeAgo());
    }
}
