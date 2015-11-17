package org.openstack.android.openstacksummit.common.data_access.deserialization;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openstack.android.openstacksummit.common.entities.PresentationSpeaker;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by Claudio Redi on 11/16/2015.
 */
@RunWith(RobolectricTestRunner.class)
public class PresentationSpeakerDeserializerTests {

    @Test
    public void deserialize_validJSON_returnsCorrectInstance() throws JSONException {
        // Arrange
        String jsonString = "{\"id\":8,\"first_name\":\"Anthony\",\"last_name\":\"Bettini\",\"title\":\"Founder & CEO\",\"bio\":\"<p>Anthony Bettini is the Founder &amp; CEO of FlawCheck.<\\/p>\",\"irc\":\"irc\",\"twitter\":\"AnthonyBettini\",\"member_id\":36219,\"presentations\":[3591],\"pic\":\"http://pic.com/pic.jpg\",\"gender\":null}";
        PersonDeserializer personDeserializer = new PersonDeserializer();
        IPresentationSpeakerDeserializer presentationSpeakerDeserializer = new PresentationSpeakerDeserializer(personDeserializer);

        // Act
        PresentationSpeaker presentationSpeaker = presentationSpeakerDeserializer.deserialize(jsonString);

        // Assert
        Assert.assertEquals(8, presentationSpeaker.getId());
        Assert.assertEquals("Anthony", presentationSpeaker.getFirstName());
        Assert.assertEquals("Bettini", presentationSpeaker.getLastName());
        Assert.assertEquals("Founder & CEO", presentationSpeaker.getTitle());
        Assert.assertEquals("<p>Anthony Bettini is the Founder &amp; CEO of FlawCheck.</p>", presentationSpeaker.getBio());
        //TODO
        //Assert.assertEquals("", presentationSpeaker.getEmail());
        Assert.assertEquals("irc", presentationSpeaker.getIrc());
        Assert.assertEquals(36219, (int)presentationSpeaker.getMemberId());
        Assert.assertEquals("http://pic.com/pic.jpg", presentationSpeaker.getPictureUrl());
        Assert.assertEquals("AnthonyBettini", presentationSpeaker.getTwitter());
    }

    @Test
    public void deserialize_validJSONWithAllOptionalFieldsInNull_returnsCorrectInstance() throws JSONException {
        // Arrange
        String jsonString = "{\"id\":8,\"first_name\":\"Anthony\",\"last_name\":\"Bettini\",\"title\":null,\"bio\":null,\"irc\":null,\"twitter\":null,\"member_id\":null,\"presentations\":[3591],\"pic\":null,\"gender\":null}";
        PersonDeserializer personDeserializer = new PersonDeserializer();
        IPresentationSpeakerDeserializer presentationSpeakerDeserializer = new PresentationSpeakerDeserializer(personDeserializer);

        // Act
        PresentationSpeaker presentationSpeaker = presentationSpeakerDeserializer.deserialize(jsonString);

        // Assert
        Assert.assertEquals(8, presentationSpeaker.getId());
        Assert.assertEquals("Anthony", presentationSpeaker.getFirstName());
        Assert.assertEquals("Bettini", presentationSpeaker.getLastName());
        Assert.assertNull(presentationSpeaker.getTitle());
        Assert.assertNull(presentationSpeaker.getBio());
        //TODO
        //Assert.assertEquals("", presentationSpeaker.getEmail());
        Assert.assertNull(presentationSpeaker.getIrc());
        Assert.assertNull(presentationSpeaker.getMemberId());
        Assert.assertNull(presentationSpeaker.getPictureUrl());
        Assert.assertNull(presentationSpeaker.getTwitter());
    }

    @Test
    public void deserialize_validJSONWithAllOptionalFieldsMissed_returnsCorrectInstance() throws JSONException {
        // Arrange
        String jsonString = "{\"id\":8,\"first_name\":\"Anthony\",\"last_name\":\"Bettini\"}";

        PersonDeserializer personDeserializer = new PersonDeserializer();
        IPresentationSpeakerDeserializer presentationSpeakerDeserializer = new PresentationSpeakerDeserializer(personDeserializer);

        // Act
        PresentationSpeaker presentationSpeaker = presentationSpeakerDeserializer.deserialize(jsonString);

        // Assert
        Assert.assertEquals(8, presentationSpeaker.getId());
        Assert.assertEquals("Anthony", presentationSpeaker.getFirstName());
        Assert.assertEquals("Bettini", presentationSpeaker.getLastName());
        Assert.assertNull(presentationSpeaker.getTitle());
        Assert.assertNull(presentationSpeaker.getBio());
        //TODO
        //Assert.assertEquals("", presentationSpeaker.getEmail());
        Assert.assertNull(presentationSpeaker.getIrc());
        Assert.assertNull(presentationSpeaker.getMemberId());
        Assert.assertNull(presentationSpeaker.getPictureUrl());
        Assert.assertNull(presentationSpeaker.getTwitter());
    }
}
