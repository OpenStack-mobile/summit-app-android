package org.openstack.android.openstacksummit.common.data_access.deserialization;

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

        IMemberDeserializer memberDeserializer = new MemberDeserializer(deserializerMock);

        // Act

        // Assert
    }
}
