package org.openstack.android.summit.common.data_access.deserialization;

import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * Created by Claudio Redi on 11/16/2015.
 */
public class MemberDeserializerTests {
    @Test
    public void deserialize_validJSON_returnsCorrectInstance() {
        // Arrange
        Deserializer deserializerMock = mock(Deserializer.class);
        PresentationSpeakerDeserializer presentationSpeakerDeserializer = mock(PresentationSpeakerDeserializer.class);
        SummitAttendeeDeserializer summitAttendeeDeserializer = mock(SummitAttendeeDeserializer.class);

        IMemberDeserializer memberDeserializer = new MemberDeserializer(presentationSpeakerDeserializer, summitAttendeeDeserializer);

        // Act

        // Assert
    }
}
