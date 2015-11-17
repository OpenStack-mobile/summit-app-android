package org.openstack.android.openstacksummit.common.data_access.deserialization;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openstack.android.openstacksummit.common.entities.Presentation;
import org.openstack.android.openstacksummit.common.entities.PresentationSpeaker;
import org.openstack.android.openstacksummit.common.entities.Track;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Claudio Redi on 11/16/2015.
 */
@RunWith(RobolectricTestRunner.class)
public class PresentationDeserializerTests {

    @Test
    public void deserialize_validJSON_returnsCorrectInstance() throws JSONException {
        // Arrange
        String jsonString = "{\"id\":4189,\"title\":\"A Day in the Life of an Openstack & Cloud Architect\",\"description\":\"Event Description\",\"start_date\":1445922900,\"end_date\":1445925300,\"location_id\":28,\"type_id\":1,\"class_name\":\"Presentation\",\"track_id\":5,\"moderator_speaker_id\":null,\"level\":\"Intermediate\",\"allow_feedback\":false,\"summit_types\":[1,2],\"sponsors\":[],\"speakers\":[795,1873],\"tags\":[],\"slides\":[],\"videos\":[]}";
        Deserializer deserializerMock = mock(Deserializer.class);
        DeserializerStorage deserializerStorageMock = mock(DeserializerStorage.class);
        int trackId = 5;
        Track track = new Track();
        track.setId(trackId);
        when(deserializerStorageMock.get(trackId, Track.class)).thenReturn(track);

        int speakerId1 = 795;
        PresentationSpeaker presentationSpeaker1 = new PresentationSpeaker();
        presentationSpeaker1.setId(speakerId1);
        when(deserializerStorageMock.get(speakerId1, PresentationSpeaker.class)).thenReturn(presentationSpeaker1);

        int speakerId2 = 1873;
        PresentationSpeaker presentationSpeaker2 = new PresentationSpeaker();
        presentationSpeaker1.setId(speakerId2);
        when(deserializerStorageMock.get(speakerId2, PresentationSpeaker.class)).thenReturn(presentationSpeaker2);

        PresentationDeserializer presentationDeserializer = new PresentationDeserializer(deserializerMock, deserializerStorageMock);

        // Act
        Presentation presentation = presentationDeserializer.deserialize(jsonString);

        // Assert
        Assert.assertEquals(4189, presentation.getId());
        Assert.assertEquals("Intermediate", presentation.getLevel());
        Assert.assertEquals(track, presentation.getTrack());
        Assert.assertEquals(2, presentation.getSpeakers().size());
        Assert.assertEquals(presentationSpeaker1, presentation.getSpeakers().get(0));
        Assert.assertEquals(presentationSpeaker2, presentation.getSpeakers().get(1));
    }

    @Test
    public void deserialize_invalidJSONWithAllRequiredFieldsMissled_throwsJSONException() throws JSONException {
        String jsonString = "{}";

        Deserializer deserializerMock = mock(Deserializer.class);
        DeserializerStorage deserializerStorageMock = mock(DeserializerStorage.class);

        PresentationDeserializer presentationDeserializer = new PresentationDeserializer(deserializerMock, deserializerStorageMock);
        String exceptionMessage = "";
        int exceptionCount = 0;
        int expectedExceptionCount = 1;

        // Act
        try {
            presentationDeserializer.deserialize(jsonString);
        }
        catch (JSONException ex) {
            exceptionMessage = ex.getMessage();
            exceptionCount++;
        }

        // Assert
        Assert.assertEquals(exceptionCount, expectedExceptionCount);
        Assert.assertEquals("Following fields are missed id", exceptionMessage);
    }

    @Test
    public void deserialize_validJSONWithAllOptionalFieldsInNull_returnsCorrectInstance() throws JSONException {
        // Arrange
        String jsonString = "{\"id\":4189,\"title\":\"A Day in the Life of an Openstack & Cloud Architect\",\"description\":\"Event Description\",\"start_date\":1445922900,\"end_date\":1445925300,\"location_id\":28,\"type_id\":1,\"class_name\":\"Presentation\",\"track_id\":5,\"moderator_speaker_id\":null,\"level\":null,\"allow_feedback\":false,\"summit_types\":[1,2],\"sponsors\":[],\"speakers\":[795,1873],\"tags\":[],\"slides\":[],\"videos\":[]}";
        Deserializer deserializerMock = mock(Deserializer.class);
        DeserializerStorage deserializerStorageMock = mock(DeserializerStorage.class);
        int trackId = 5;
        Track track = new Track();
        track.setId(trackId);
        when(deserializerStorageMock.get(trackId, Track.class)).thenReturn(track);

        int speakerId1 = 795;
        PresentationSpeaker presentationSpeaker1 = new PresentationSpeaker();
        presentationSpeaker1.setId(speakerId1);
        when(deserializerStorageMock.get(speakerId1, PresentationSpeaker.class)).thenReturn(presentationSpeaker1);

        int speakerId2 = 1873;
        PresentationSpeaker presentationSpeaker2 = new PresentationSpeaker();
        presentationSpeaker1.setId(speakerId2);
        when(deserializerStorageMock.get(speakerId2, PresentationSpeaker.class)).thenReturn(presentationSpeaker2);

        PresentationDeserializer presentationDeserializer = new PresentationDeserializer(deserializerMock, deserializerStorageMock);

        // Act
        Presentation presentation = presentationDeserializer.deserialize(jsonString);

        // Assert
        Assert.assertEquals(4189, presentation.getId());
        Assert.assertNull(presentation.getLevel());
        Assert.assertEquals(track, presentation.getTrack());
        Assert.assertEquals(2, presentation.getSpeakers().size());
        Assert.assertEquals(presentationSpeaker1, presentation.getSpeakers().get(0));
        Assert.assertEquals(presentationSpeaker2, presentation.getSpeakers().get(1));
    }
}
