package org.openstack.android.summit.common.data_access.deserialization;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Claudio Redi on 11/12/2015.
 */
@RunWith(RobolectricTestRunner.class)
public class FeedbackDeserializerTests {
    @Test
    public void deserialize_validJSON_returnsCorrectInstance() throws Exception {
        // Arrange
        String jsonString = "{\"id\":1,\"rate\":5,\"note\":\"test review\",\"owner_id\":2,\"created_date\": 1447686387,\"event_id\":1}";

        DeserializerStorage deserializerStorageMock = mock(DeserializerStorage.class);
        SummitAttendee summitAttendee = new SummitAttendee();
        summitAttendee.setId(2);
        when(deserializerStorageMock.get(2, SummitAttendee.class)).thenReturn(summitAttendee);
        IFeedbackDeserializer feedbackDeserializer = new FeedbackDeserializer(deserializerStorageMock);

        // Act
        Feedback feedback = feedbackDeserializer.deserialize(jsonString);

        // Assert
        Assert.assertEquals(1, feedback.getId());
        Assert.assertEquals(5, feedback.getRate());
        Assert.assertEquals("test review", feedback.getReview());
        Assert.assertEquals(2, feedback.getOwner().getId());
    }

    @Test
    public void deserialize_invalidJSONWithAllRequiredFieldsMissled_throwsJSONException() throws JSONException {
        String jsonString = "{}";
        JSONObject jsonObjectMock = mock(JSONObject.class);
        when(jsonObjectMock.has(any(String.class))).thenReturn(false);

        DeserializerStorage deserializerStorageMock = mock(DeserializerStorage.class);

        IFeedbackDeserializer feedbackDeserializer = new FeedbackDeserializer(deserializerStorageMock);
        String exceptionMessage = "";
        int exceptionCount = 0;
        int expectedExceptionCount = 1;

        // Act
        try {
            feedbackDeserializer.deserialize(jsonString);
        }
        catch (JSONException ex) {
            exceptionMessage = ex.getMessage();
            exceptionCount++;
        }

        // Assert
        Assert.assertEquals(exceptionCount, expectedExceptionCount);
        Assert.assertEquals("Following fields are missed id, rate, created_date, event_id", exceptionMessage);
    }

    @Test
    public void deserialize_validJSONWithAllOptionalFieldsInNull_returnsCorrectInstance() throws Exception {
        // Arrange
        String jsonString = "{\"id\":1,\"rate\":5,\"review\":null,\"owner_id\":2,\"created_date\": 1447686387,\"event_id\":1}";

        DeserializerStorage deserializerStorageMock = mock(DeserializerStorage.class);
        SummitAttendee summitAttendee = new SummitAttendee();
        summitAttendee.setId(2);
        when(deserializerStorageMock.get(2, SummitAttendee.class)).thenReturn(summitAttendee);
        IFeedbackDeserializer feedbackDeserializer = new FeedbackDeserializer(deserializerStorageMock);

        // Act
        Feedback feedback = feedbackDeserializer.deserialize(jsonString);

        // Assert
        Assert.assertEquals(1, feedback.getId());
        Assert.assertEquals(5, feedback.getRate());
        Assert.assertNull(feedback.getReview());
        Assert.assertEquals(2, feedback.getOwner().getId());
    }
}
