package org.openstack.android.summit.common.data_access.data_polling;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openstack.android.summit.common.entities.EventType;
import org.openstack.android.summit.common.entities.Presentation;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.SummitType;
import org.openstack.android.summit.common.entities.TicketType;
import org.openstack.android.summit.common.entities.Track;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.entities.VenueRoom;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by Claudio Redi on 2/8/2016.
 */
@RunWith(RobolectricTestRunner.class)
public class ClassResolverTests {
    @Test
    public void fromName_Presentation_returnsCorrectType() throws ClassNotFoundException {
        // Arrange
        ClassResolver classResolver = new ClassResolver();
        String className = "Presentation";

        // Act
        Class type = classResolver.fromName(className);

        // Assert
        Assert.assertEquals(type, SummitEvent.class);
    }

    @Test
    public void fromName_SummitType_returnsCorrectType() throws ClassNotFoundException {
        // Arrange
        ClassResolver classResolver = new ClassResolver();
        String className = "SummitType";

        // Act
        Class type = classResolver.fromName(className);

        // Assert
        Assert.assertEquals(type, SummitType.class);
    }

    @Test
    public void fromName_SummitEventType_returnsCorrectType() throws ClassNotFoundException {
        // Arrange
        ClassResolver classResolver = new ClassResolver();
        String className = "SummitEventType";

        // Act
        Class type = classResolver.fromName(className);

        // Assert
        Assert.assertEquals(type, EventType.class);
    }

    @Test
    public void fromName_PresentationSpeaker_returnsCorrectType() throws ClassNotFoundException {
        // Arrange
        ClassResolver classResolver = new ClassResolver();
        String className = "PresentationSpeaker";

        // Act
        Class type = classResolver.fromName(className);

        // Assert
        Assert.assertEquals(type, PresentationSpeaker.class);
    }

    @Test
    public void fromName_SummitTicketType_returnsCorrectType() throws ClassNotFoundException {
        // Arrange
        ClassResolver classResolver = new ClassResolver();
        String className = "SummitTicketType";

        // Act
        Class type = classResolver.fromName(className);

        // Assert
        Assert.assertEquals(type, TicketType.class);
    }

    @Test
    public void fromName_SummitVenue_returnsCorrectType() throws ClassNotFoundException {
        // Arrange
        ClassResolver classResolver = new ClassResolver();
        String className = "SummitVenue";

        // Act
        Class type = classResolver.fromName(className);

        // Assert
        Assert.assertEquals(type, Venue.class);
    }

    @Test
    public void fromName_SummitVenueRoom_returnsCorrectType() throws ClassNotFoundException {
        // Arrange
        ClassResolver classResolver = new ClassResolver();
        String className = "SummitVenueRoom";

        // Act
        Class type = classResolver.fromName(className);

        // Assert
        Assert.assertEquals(type, VenueRoom.class);
    }

    @Test
    public void fromName_PresentationCategory_returnsCorrectType() throws ClassNotFoundException {
        // Arrange
        ClassResolver classResolver = new ClassResolver();
        String className = "PresentationCategory";

        // Act
        Class type = classResolver.fromName(className);

        // Assert
        Assert.assertEquals(type, Track.class);
    }

    @Test
    public void fromName_MySchedule_returnsCorrectType() throws ClassNotFoundException {
        // Arrange
        ClassResolver classResolver = new ClassResolver();
        String className = "MySchedule";

        // Act
        Class type = classResolver.fromName(className);

        // Assert
        Assert.assertEquals(type, SummitEvent.class);
    }
}
