package org.openstack.android.summit.common.data_access.deserialization;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.entities.TrackGroup;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Claudio Redi on 1/20/2016.
 */
@RunWith(RobolectricTestRunner.class)
public class TrackGroupTests {
    @Test
    public void deserialize_validJSONWithOneTrack_returnsCorrectInstance() throws JSONException {
        // Arrange
        String jsonString = "{\"id\":2,\"name\":\"CIO / Business Leader\",\"color\":\"#0d37c3\",\"description\":\"track group description\",\"tracks\":[\"2\"]}";

        int trackId = 2;
        Track track = new Track();
        track.setId(trackId);

        TrackDeserializer trackDeserializerMock = mock(TrackDeserializer.class);
        TrackGroupDeserializer trackGroupDeserializer = new TrackGroupDeserializer(trackDeserializerMock);

        // Act
        TrackGroup trackGroup = trackGroupDeserializer.deserialize(jsonString);

        // Assert
        Assert.assertEquals(2, trackGroup.getId());
        Assert.assertEquals("CIO / Business Leader", trackGroup.getName());
        Assert.assertEquals("track group description", trackGroup.getDescription());
        Assert.assertEquals("#0d37c3", trackGroup.getColor());
    }

    @Test
    public void deserialize_validJSONWithNoTrack_returnsCorrectInstance() throws JSONException {
        // Arrange
        String jsonString = "{\"id\":2,\"name\":\"CIO / Business Leader\",\"color\":\"#0d37c3\",\"description\":\"track group description\",\"tracks\":[]}";

        int trackId = 2;
        Track track = new Track();
        track.setId(trackId);

        TrackDeserializer trackDeserializerMock = mock(TrackDeserializer.class);
        TrackGroupDeserializer trackGroupDeserializer = new TrackGroupDeserializer(trackDeserializerMock);

        // Act
        TrackGroup trackGroup = trackGroupDeserializer.deserialize(jsonString);

        // Assert
        Assert.assertEquals(2, trackGroup.getId());
        Assert.assertEquals("CIO / Business Leader", trackGroup.getName());
        Assert.assertEquals("track group description", trackGroup.getDescription());
        Assert.assertEquals("#0d37c3", trackGroup.getColor());
    }
}
