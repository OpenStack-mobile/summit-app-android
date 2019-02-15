package org.openstack.android.summit.common.data_access.deserialization;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openstack.android.summit.common.entities.Speaker;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.mock;

/**
 * Created by Claudio Redi on 11/16/2015.
 */
@RunWith(RobolectricTestRunner.class)
public class SpeakerDeserializerTests {

    @Test
    public void deserialize_validJSON_returnsCorrectInstance() throws JSONException {
        // Arrange
        String jsonString = "{\"id\":8,\"first_name\":\"Anthony\",\"last_name\":\"Bettini\",\"title\":\"Founder & CEO\",\"bio\":\"<p>Anthony Bettini is the Founder &amp; CEO of FlawCheck.<\\/p>\",\"irc\":\"irc\",\"twitter\":\"AnthonyBettini\",\"member_id\":36219,\"presentations\":[3591],\"pic\":\"http://pic.com/pic.jpg\",\"gender\":null}";
        PersonDeserializer personDeserializer = new PersonDeserializer();

        ISpeakerDeserializer presentationSpeakerDeserializer = new SpeakerDeserializer(personDeserializer);

        // Act
        Speaker speaker = presentationSpeakerDeserializer.deserialize(jsonString);

        // Assert
        Assert.assertEquals(8, speaker.getId());
        Assert.assertEquals("Anthony", speaker.getFirstName());
        Assert.assertEquals("Bettini", speaker.getLastName());
        Assert.assertEquals("Founder & CEO", speaker.getTitle());
        Assert.assertEquals("<p>Anthony Bettini is the Founder &amp; CEO of FlawCheck.</p>", speaker.getBio());
        //TODO
        //Assert.assertEquals("", speaker.getEmail());
        Assert.assertEquals("irc", speaker.getIrc());
        Assert.assertEquals(36219, (int) speaker.getMemberId());
        Assert.assertEquals("http://pic.com/pic.jpg", speaker.getPictureUrl());
        Assert.assertEquals("AnthonyBettini", speaker.getTwitter());
    }

    @Test
    public void deserialize_validJSONWithAllOptionalFieldsInNull_returnsCorrectInstance() throws JSONException {
        // Arrange
        String jsonString = "{\"id\":8,\"first_name\":\"Anthony\",\"last_name\":\"Bettini\",\"title\":null,\"bio\":null,\"irc\":null,\"twitter\":null,\"member_id\":null,\"presentations\":[3591],\"pic\":null,\"gender\":null}";
        PersonDeserializer personDeserializer = new PersonDeserializer();
        ISpeakerDeserializer presentationSpeakerDeserializer = new SpeakerDeserializer(personDeserializer);

        // Act
        Speaker speaker = presentationSpeakerDeserializer.deserialize(jsonString);

        // Assert
        Assert.assertEquals(8, speaker.getId());
        Assert.assertEquals("Anthony", speaker.getFirstName());
        Assert.assertEquals("Bettini", speaker.getLastName());
        Assert.assertNull(speaker.getTitle());
        Assert.assertNull(speaker.getBio());
        //TODO
        //Assert.assertEquals("", speaker.getEmail());
        Assert.assertNull(speaker.getIrc());
        Assert.assertNull(speaker.getMemberId());
        Assert.assertNull(speaker.getPictureUrl());
        Assert.assertNull(speaker.getTwitter());
    }

    @Test
    public void deserialize_validJSONWithAllOptionalFieldsMissed_returnsCorrectInstance() throws JSONException {
        // Arrange
        String jsonString = "{\"id\":8,\"first_name\":\"Anthony\",\"last_name\":\"Bettini\"}";

        PersonDeserializer personDeserializer = new PersonDeserializer();

        ISpeakerDeserializer presentationSpeakerDeserializer = new SpeakerDeserializer(personDeserializer);

        // Act
        Speaker speaker = presentationSpeakerDeserializer.deserialize(jsonString);

        // Assert
        Assert.assertEquals(8, speaker.getId());
        Assert.assertEquals("Anthony", speaker.getFirstName());
        Assert.assertEquals("Bettini", speaker.getLastName());
        Assert.assertNull(speaker.getTitle());
        Assert.assertNull(speaker.getBio());
        //TODO
        //Assert.assertEquals("", speaker.getEmail());
        Assert.assertNull(speaker.getIrc());
        Assert.assertNull(speaker.getMemberId());
        Assert.assertNull(speaker.getPictureUrl());
        Assert.assertNull(speaker.getTwitter());
    }
}
