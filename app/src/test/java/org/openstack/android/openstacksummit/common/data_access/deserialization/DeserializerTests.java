package org.openstack.android.openstacksummit.common.data_access.deserialization;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Test;
import org.openstack.android.openstacksummit.common.entities.Company;
import org.openstack.android.openstacksummit.common.entities.EventType;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Claudio Redi on 11/5/2015.
 */

public class DeserializerTests  {

    @Test
    public void deserialize_companyAndJSONIsValid_returnsCorrectCompanyInstance() throws JSONException {
        // Arrange
        Company company = new Company();
        company.setId(1);
        company.setName("test");
        String jsonString = "{ \"id\":1, \"name\":\"test\"}";
        GenericDeserializer genericDeserializerMock = mock(GenericDeserializer.class);
        when(genericDeserializerMock.deserialize(jsonString, Company.class)).thenReturn(company);
        Deserializer deserializer = new Deserializer();
        deserializer.genericDeserializer = genericDeserializerMock;

        // Act
        Company deserializedCompany = deserializer.deserialize(jsonString, Company.class);

        // Assert
        Assert.assertEquals(company, deserializedCompany);
    }

    @Test
    public void deserialize_eventTypeAndJSONIsValid_returnsCorrectEventTypeInstance() throws JSONException {
        // Arrange
        EventType eventType = new EventType();
        eventType.setId(1);
        eventType.setName("test");
        String jsonString = "{ \"id\":1, \"name\":\"test\"}";
        GenericDeserializer genericDeserializerMock = mock(GenericDeserializer.class);
        when(genericDeserializerMock.deserialize(jsonString, EventType.class)).thenReturn(eventType);
        Deserializer deserializer = new Deserializer();
        deserializer.genericDeserializer = genericDeserializerMock;

        // Act
        EventType deserializedEventType = deserializer.deserialize(jsonString, EventType.class);

        // Assert
        Assert.assertEquals(eventType, deserializedEventType);
    }
}