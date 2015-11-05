package org.openstack.android.openstacksummit.common.data_access.deserialization;

import android.app.Application;
import android.test.ApplicationTestCase;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;
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
        String json = "{ \"id\":1, \"name\":\"test\"}";
        JSONObject jsonObject = new JSONObject(json);
        CompanyDeserializer companyDeserializerMock = mock(CompanyDeserializer.class);
        when(companyDeserializerMock.deserialize(jsonObject)).thenReturn(company);
        Deserializer deserializer = new Deserializer();
        deserializer.companyDeserializer = companyDeserializerMock;

        // Act
        Company deserializedCompany = (Company)deserializer.deserialize(jsonObject, Company.class);

        // Assert
        Assert.assertEquals(company, deserializedCompany);
    }

    @Test
    public void deserialize_eventTypeAndJSONIsValid_returnsCorrectEventTypeInstance() throws JSONException {
        // Arrange
        EventType eventType = new EventType();
        eventType.setId(1);
        eventType.setName("test");
        String json = "{ \"id\":1, \"name\":\"test\"}";
        JSONObject jsonObject = new JSONObject(json);
        EventTypeDeserializer eventTypeDeserializerMock = mock(EventTypeDeserializer.class);
        when(eventTypeDeserializerMock.deserialize(jsonObject)).thenReturn(eventType);
        Deserializer deserializer = new Deserializer();
        deserializer.eventTypeDeserializer = eventTypeDeserializerMock;

        // Act
        EventType deserializedEventType = (EventType)deserializer.deserialize(jsonObject, EventType.class);

        // Assert
        Assert.assertEquals(eventType, deserializedEventType);
    }
}