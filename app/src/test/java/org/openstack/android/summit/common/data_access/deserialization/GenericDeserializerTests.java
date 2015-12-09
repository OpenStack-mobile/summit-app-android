package org.openstack.android.summit.common.data_access.deserialization;

import junit.framework.Assert;

import org.junit.Test;
import org.openstack.android.summit.common.entities.Company;
import org.openstack.android.summit.common.entities.EventType;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Claudio Redi on 11/12/2015.
 */
public class GenericDeserializerTests {
    @Test
    public void deserialize_validJsonForCompany_returnsCorrectCompanyInstance() {
        // Arrange
        DeserializerStorage deserializerStorageMock = mock(DeserializerStorage.class);
        GenericDeserializer deserializer = new GenericDeserializer(deserializerStorageMock);
        String jsonString = "{ \"id\":1, \"name\":\"test\"}";

        // Act
        Company company = deserializer.deserialize(jsonString, Company.class);

        // Assert
        Assert.assertEquals(1, company.getId());
        Assert.assertEquals("test", company.getName());
    }

    @Test
    public void deserialize_validJsonForEventType_returnsCorrectEventTypeInstance() {
        // Arrange
        DeserializerStorage deserializerStorageMock = mock(DeserializerStorage.class);
        GenericDeserializer deserializer = new GenericDeserializer(deserializerStorageMock);
        String jsonString = "{ \"id\":1, \"name\":\"test\"}";

        // Act
        EventType eventType = deserializer.deserialize(jsonString, EventType.class);

        // Assert
        Assert.assertEquals(1, eventType.getId());
        Assert.assertEquals("test", eventType.getName());
    }

    @Test
    public void deserialize_validJsonForEventType_addsEntityOnDeserializerStorage() {
        // Arrange
        DeserializerStorage deserializerStorageMock = mock(DeserializerStorage.class);
        GenericDeserializer deserializer = new GenericDeserializer(deserializerStorageMock);
        String jsonString = "{ \"id\":1, \"name\":\"test\"}";

        // Act
        EventType eventType = deserializer.deserialize(jsonString, EventType.class);

        // Assert
        verify(deserializerStorageMock, times(1)).add(eventType, EventType.class);
    }
}
