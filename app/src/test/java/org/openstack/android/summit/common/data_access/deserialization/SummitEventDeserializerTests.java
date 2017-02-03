package org.openstack.android.summit.common.data_access.deserialization;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openstack.android.summit.common.entities.Company;
import org.openstack.android.summit.common.entities.EventType;
import org.openstack.android.summit.common.entities.Presentation;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.SummitType;
import org.openstack.android.summit.common.entities.Tag;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.entities.VenueRoom;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Claudio Redi on 11/17/2015.
 */
@RunWith(RobolectricTestRunner.class)
public class SummitEventDeserializerTests {
    @Test
    public void deserialize_validJSONForPresentationEventAndVenueRoomAssigned_returnsCorrectInstance() throws JSONException {
        // Arrange
        String jsonString = "{\"id\":4189,\"title\":\"A Day in the Life of an Openstack & Cloud Architect\",\"description\":\"Event Description\",\"start_date\":1445922900,\"end_date\":1445925300,\"location_id\":28,\"type_id\":1,\"class_name\":\"Presentation\",\"track_id\":5,\"moderator_speaker_id\":null,\"level\":\"Intermediate\",\"allow_feedback\":true,\"summit_types\":[1,2],\"sponsors\":[5],\"speakers\":[795,1873],\"tags\":[{\"id\":19,\"tag\":\"test_tag\"}],\"slides\":[],\"videos\":[]}";
        int summitTypeId1 = 1;
        SummitType summitType1 = new SummitType();
        summitType1.setId(summitTypeId1);

        int summitTypeId2 = 2;
        SummitType summitType2 = new SummitType();
        summitType2.setId(summitTypeId2);

        int companyId = 5;
        Company company = new Company();
        company.setId(companyId);

        int venueRoomId = 28;
        VenueRoom venueRoom = new VenueRoom();
        venueRoom.setId(venueRoomId);

        int eventTypeId = 1;
        EventType eventType = new EventType();
        eventType.setId(eventTypeId);

        GenericDeserializer genericDeserializerMock = mock(GenericDeserializer.class);
        int tagId = 5;
        Tag tag = new Tag();
        tag.setId(tagId);
        when(genericDeserializerMock.deserialize(eq("{\"id\":19,\"tag\":\"test_tag\"}"), eq(Tag.class))).thenReturn(tag);

        PresentationDeserializer presentationDeserializerMock = mock(PresentationDeserializer.class);
        int presentationId = 50;
        Presentation presentation = new Presentation();
        presentation.setId(presentationId);
        when(presentationDeserializerMock.deserialize(eq(jsonString))).thenReturn(presentation);

        Summit summit = new Summit();
        summit.setId(1);
        ArrayList<Summit> summits = new ArrayList<>();
        summits.add(summit);
        SummitGroupEventDeserializer summitGroupEventDeserializer = mock(SummitGroupEventDeserializer.class);
        SummitEventDeserializer summitEventDeserializer = new SummitEventDeserializer(genericDeserializerMock, presentationDeserializerMock, summitGroupEventDeserializer);

        // Act
        SummitEvent summitEvent = summitEventDeserializer.deserialize(jsonString);

        // Assert
        Assert.assertEquals(4189, summitEvent.getId());
        Assert.assertEquals("A Day in the Life of an Openstack & Cloud Architect", summitEvent.getName());
        Assert.assertEquals("Event Description", summitEvent.getDescription());
        Assert.assertTrue(summitEvent.getAllowFeedback());
        Assert.assertEquals(1445922900000L, summitEvent.getStart().getTime());
        Assert.assertEquals(1445925300000L, summitEvent.getEnd().getTime());
        Assert.assertEquals(eventType, summitEvent.getType());
        Assert.assertEquals(venueRoom, summitEvent.getVenueRoom());
        Assert.assertNull(summitEvent.getVenue());
        Assert.assertEquals(1, summitEvent.getTags().size());
        Assert.assertEquals(tag, summitEvent.getTags().get(0));
        Assert.assertEquals(1, summitEvent.getSponsors().size());
        Assert.assertEquals(company, summitEvent.getSponsors().get(0));
        Assert.assertEquals(presentation, summitEvent.getPresentation());
        Assert.assertEquals(4189, summitEvent.getPresentation().getEvent().getId());
    }

    @Test
    public void deserialize_validJSONForNonPresentationEventAndVenueAssigned_returnsCorrectInstance() throws JSONException {
        // Arrange
        String jsonString = "{\"id\":4189,\"title\":\"A Day in the Life of an Openstack & Cloud Architect\",\"description\":\"Event Description\",\"start_date\":1445922900,\"end_date\":1445925300,\"location_id\":28,\"type_id\":2,\"class_name\":\"Presentation\",\"track_id\":null,\"moderator_speaker_id\":null,\"level\":\"Intermediate\",\"allow_feedback\":false,\"summit_types\":[1,2],\"sponsors\":[],\"speakers\":[],\"tags\":[{\"id\":19,\"tag\":\"test_tag\"}],\"slides\":[],\"videos\":[]}";
        int summitTypeId1 = 1;
        SummitType summitType1 = new SummitType();
        summitType1.setId(summitTypeId1);

        int summitTypeId2 = 2;
        SummitType summitType2 = new SummitType();
        summitType2.setId(summitTypeId2);

        int venueId = 28;
        Venue venue = new Venue();
        venue.setId(venueId);

        int eventTypeId = 2;
        EventType eventType = new EventType();
        eventType.setId(eventTypeId);

        GenericDeserializer genericDeserializerMock = mock(GenericDeserializer.class);
        int tagId = 5;
        Tag tag = new Tag();
        tag.setId(tagId);
        when(genericDeserializerMock.deserialize(eq("{\"id\":19,\"tag\":\"test_tag\"}"), eq(Tag.class))).thenReturn(tag);

        PresentationDeserializer presentationDeserializerMock = mock(PresentationDeserializer.class);
        SummitGroupEventDeserializer summitGroupEventDeserializer = mock(SummitGroupEventDeserializer.class);

        SummitEventDeserializer summitEventDeserializer = new SummitEventDeserializer(genericDeserializerMock, presentationDeserializerMock, summitGroupEventDeserializer);

        Summit summit = new Summit();
        summit.setId(1);
        ArrayList<Summit> summits = new ArrayList<>();
        summits.add(summit);

        // Act
        SummitEvent summitEvent = summitEventDeserializer.deserialize(jsonString);

        // Assert
        Assert.assertEquals(4189, summitEvent.getId());
        Assert.assertEquals("A Day in the Life of an Openstack & Cloud Architect", summitEvent.getName());
        Assert.assertEquals("Event Description", summitEvent.getDescription());
        Assert.assertFalse(summitEvent.getAllowFeedback());
        Assert.assertEquals(1445922900000L, summitEvent.getStart().getTime());
        Assert.assertEquals(1445925300000L, summitEvent.getEnd().getTime());
        Assert.assertEquals(eventType, summitEvent.getType());
        Assert.assertEquals(venue, summitEvent.getVenue());
        Assert.assertNull(summitEvent.getVenueRoom());
        Assert.assertEquals(1, summitEvent.getTags().size());
        Assert.assertEquals(tag, summitEvent.getTags().get(0));
        Assert.assertEquals(0, summitEvent.getSponsors().size());
        Assert.assertNull(summitEvent.getPresentation());
    }

    @Test
    public void deserialize_invalidJSONWithAllRequiredFieldsMissled_throwsJSONException() throws JSONException {
        String jsonString = "{}";

        GenericDeserializer genericDeserializerMock = mock(GenericDeserializer.class);
        PresentationDeserializer presentationDeserializerMock = mock(PresentationDeserializer.class);
        SummitGroupEventDeserializer summitGroupEventDeserializer = mock(SummitGroupEventDeserializer.class);

        SummitEventDeserializer summitEventDeserializer = new SummitEventDeserializer(genericDeserializerMock, presentationDeserializerMock, summitGroupEventDeserializer);
        String exceptionMessage = "";
        int exceptionCount = 0;
        int expectedExceptionCount = 1;

        // Act
        try {
            summitEventDeserializer.deserialize(jsonString);
        }
        catch (JSONException ex) {
            exceptionMessage = ex.getMessage();
            exceptionCount++;
        }

        // Assert
        Assert.assertEquals(exceptionCount, expectedExceptionCount);
        Assert.assertEquals("Following fields are missed id, start_date, end_date, title, allow_feedback, type_id", exceptionMessage);
    }

    @Test
    public void deserialize_validJSONWithNullDescription_returnsInstanceWithEmptyDescription() throws JSONException {
        // Arrange
        String jsonString = "{\"id\":4189,\"title\":\"A Day in the Life of an Openstack & Cloud Architect\",\"description\":null,\"start_date\":1445922900,\"end_date\":1445925300,\"location_id\":28,\"type_id\":2,\"class_name\":\"Presentation\",\"track_id\":null,\"moderator_speaker_id\":null,\"level\":\"Intermediate\",\"allow_feedback\":false,\"summit_types\":[1,2],\"sponsors\":[],\"speakers\":[],\"tags\":[{\"id\":19,\"tag\":\"test_tag\"}],\"slides\":[],\"videos\":[]}";

        int summitTypeId1 = 1;
        SummitType summitType1 = new SummitType();
        summitType1.setId(summitTypeId1);

        int summitTypeId2 = 2;
        SummitType summitType2 = new SummitType();
        summitType2.setId(summitTypeId2);

        int venueId = 28;
        Venue venue = new Venue();
        venue.setId(venueId);

        int eventTypeId = 2;
        EventType eventType = new EventType();
        eventType.setId(eventTypeId);

        GenericDeserializer genericDeserializerMock = mock(GenericDeserializer.class);
        int tagId = 5;
        Tag tag = new Tag();
        tag.setId(tagId);
        when(genericDeserializerMock.deserialize(eq("{\"id\":19,\"tag\":\"test_tag\"}"), eq(Tag.class))).thenReturn(tag);

        Summit summit = new Summit();
        summit.setId(1);
        ArrayList<Summit> summits = new ArrayList<>();
        summits.add(summit);

        PresentationDeserializer presentationDeserializerMock = mock(PresentationDeserializer.class);
        SummitGroupEventDeserializer summitGroupEventDeserializer = mock(SummitGroupEventDeserializer.class);
        SummitEventDeserializer summitEventDeserializer = new SummitEventDeserializer(genericDeserializerMock, presentationDeserializerMock, summitGroupEventDeserializer);

        // Act
        SummitEvent summitEvent = summitEventDeserializer.deserialize(jsonString);

        // Assert
        Assert.assertEquals("", summitEvent.getDescription());
    }

}
