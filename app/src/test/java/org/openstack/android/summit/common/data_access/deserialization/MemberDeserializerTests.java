package org.openstack.android.summit.common.data_access.deserialization;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Claudio Redi on 11/16/2015.
 */
@RunWith(RobolectricTestRunner.class)
public class MemberDeserializerTests {
    @Test
    public void deserialize_validJSONForMemberWithRolesAttendeeAndSpeaker_returnsCorrectInstance() throws JSONException {
        // Arrange
        String jsonString = "{\"id\":1,\"summit_hall_checked_in\":false,\"summit_hall_checked_in_date\":1450324009,\"shared_contact_info\":true,\"ticket_type_id\":1,\"member_id\":11624,\"schedule\":[6769,6810,5648,5148,6762,5950,6827,5672,6648,4804,6829,6765,6782,3874,6802,6796,6763,6817,6783,6804,6805,6811,5014,6818,6169,6116,4733,6807,5995,4189,6560,5197,6799,6823,3659,6821,4662],\"first_name\":\"Sebastian\",\"last_name\":\"Marcet\",\"gender\":\"Male\",\"bio\":\"<p>This is my bio. For now this is here only for testing purposes so stop reading now, there is nothing interesting here.<\\/p>\",\"pic\":\"https:\\/\\/devbranch.openstack.org\\/profile_images\\/members\\/11624\",\"linked_in\":\"linkedin.com\\/sebastian.marcet\",\"irc\":\"sebastian.irc\",\"twitter\":\"smarcet\",\"speaker\":{\"id\":9,\"first_name\":\"Sebastian\",\"last_name\":\"Marcet\",\"title\":\"VP of Awesome\",\"bio\":\"<p>This is my bio. For now this is here only for testing purposes so stop reading now, there is nothing interesting here.<\\/p>\",\"irc\":\"sebastian.irc\",\"twitter\":\"smarcet\",\"member_id\":11624,\"presentations\":[6834,6835,6841],\"pic\":\"https:\\/\\/devbranch.openstack.org\\/profile_images\\/speakers\\/9\",\"gender\":\"Male\"},\"feedback\":[{\"id\":1,\"rate\":2,\"note\":\"test\",\"event_id\":6805,\"created_date\":1448478672,\"owner_id\":1},{\"id\":2,\"rate\":4,\"note\":\"Ffffffffffff\",\"event_id\":6807,\"created_date\":1448488029,\"owner_id\":1},{\"id\":3,\"rate\":5,\"note\":\"Malis quando id mel, ea usu dolor malorum, numquam democritum at duo. An quo prompta quaestio. Id vix sonet posidonium. In dicta saepe adolescens est. Id tibique convenire definiebas vix. Ne tritani intellegat mediocritatem his.\",\"event_id\":6805,\"created_date\":1448653212,\"owner_id\":1},{\"id\":4,\"rate\":4,\"note\":\"Meliore singulis id has, ex errem omittantur appellantur ius. Te qui elit pertinax, te reprimique signiferumque per. Sit consequat efficiendi scripserit ad, in quo stet delenit atomorum, vim in novum consequat. Iisque aliquid dignissim eos ea, id nec illud corrumpit. Qui eu idque epicurei nominati.\",\"event_id\":6805,\"created_date\":1448655561,\"owner_id\":1},{\"id\":5,\"rate\":2,\"note\":\"Meliore singulis id has, ex errem omittantur appellantur ius. Te qui elit pertinax, te reprimique signiferumque per. Sit consequat efficiendi scripserit ad, in quo stet delenit atomorum, vim in novum consequat. Iisque aliquid dignissim eos ea, id nec illud corrumpit. Qui eu idque epicurei nominati.Meliore singulis id has, ex errem omittantur appellantur ius. Te qui elit pertinax, te reprimique signiferumque per. Sit consequat efficiendi\",\"event_id\":6805,\"created_date\":1448655662,\"owner_id\":1},{\"id\":6,\"rate\":3,\"note\":\"TESTING FEEDBACK\",\"event_id\":6805,\"created_date\":1448655679,\"owner_id\":1},{\"id\":7,\"rate\":1,\"note\":\"Meliore singulis id has, ex errem omittantur appellantur ius. Te qui elit pertinax, te reprimique signiferumque per. Sit consequat efficiendi scripserit ad, in quo stet delenit atomorum, vim in novum consequat. Iisque aliquid dignissim eos ea, id nec illud corrumpit. Qui eu idque epicurei nominati.\",\"event_id\":6805,\"created_date\":1448660527,\"owner_id\":1},{\"id\":8,\"rate\":5,\"note\":\"Testigo feedback feedbackfe edbackfeedback feedback feedback feedback\",\"event_id\":3874,\"created_date\":1448904646,\"owner_id\":1},{\"id\":9,\"rate\":3,\"note\":\"testing testing testing testing testing testing testing testing teinsfdf ested tesdsd sd d sdsdsd sdsd sd sdadsd\",\"event_id\":4803,\"created_date\":1448906446,\"owner_id\":1},{\"id\":10,\"rate\":4,\"note\":\"This ruled\",\"event_id\":5014,\"created_date\":1448946327,\"owner_id\":1},{\"id\":11,\"rate\":5,\"note\":\"Great job\",\"event_id\":6821,\"created_date\":1449082907,\"owner_id\":1},{\"id\":12,\"rate\":3,\"note\":\"Boo boo\",\"event_id\":6828,\"created_date\":1449083318,\"owner_id\":1},{\"id\":13,\"rate\":4,\"note\":\"Great feedback!\",\"event_id\":6823,\"created_date\":1449167147,\"owner_id\":1},{\"id\":14,\"rate\":5,\"note\":\"Wow\",\"event_id\":6169,\"created_date\":1449352059,\"owner_id\":1},{\"id\":15,\"rate\":3,\"note\":\"Hahaha\",\"event_id\":3659,\"created_date\":1450124954,\"owner_id\":1},{\"id\":16,\"rate\":4,\"note\":\"Love it\",\"event_id\":6821,\"created_date\":1450197125,\"owner_id\":1}]}";
        String speakerJsonString = new JSONObject(jsonString).getJSONObject("speaker").toString();
        PresentationSpeaker presentationSpeaker = new PresentationSpeaker();
        PresentationSpeakerDeserializer presentationSpeakerDeserializer = mock(PresentationSpeakerDeserializer.class);
        when(presentationSpeakerDeserializer.deserialize(eq(speakerJsonString))).thenReturn(presentationSpeaker);

        SummitAttendeeDeserializer summitAttendeeDeserializer = mock(SummitAttendeeDeserializer.class);
        PersonDeserializer personDeserializer = mock(PersonDeserializer.class);
        FeedbackDeserializer feedbackDeserializer = mock(FeedbackDeserializer.class);

        SummitAttendee summitAttendee = new SummitAttendee();
        when(summitAttendeeDeserializer.deserialize(eq(jsonString))).thenReturn(summitAttendee);

        IMemberDeserializer memberDeserializer = new MemberDeserializer(personDeserializer,presentationSpeakerDeserializer, summitAttendeeDeserializer, feedbackDeserializer);

        // Act
        Member member = memberDeserializer.deserialize(jsonString);

        // Assert
        Assert.assertEquals(1, member.getId());
        Assert.assertEquals(summitAttendee, member.getAttendeeRole());
        Assert.assertEquals(presentationSpeaker, member.getSpeakerRole());
    }

    @Test
    public void deserialize_validJSONForMemberWithRoleAttendee_returnsCorrectInstance() throws JSONException {
        // Arrange
        String jsonString = "{\"id\":1,\"summit_hall_checked_in\":false,\"summit_hall_checked_in_date\":1450324009,\"shared_contact_info\":true,\"ticket_type_id\":1,\"member_id\":11624,\"schedule\":[6769,6810,5648,5148,6762,5950,6827,5672,6648,4804,6829,6765,6782,3874,6802,6796,6763,6817,6783,6804,6805,6811,5014,6818,6169,6116,4733,6807,5995,4189,6560,5197,6799,6823,3659,6821,4662],\"first_name\":\"Sebastian\",\"last_name\":\"Marcet\",\"gender\":\"Male\",\"bio\":\"<p>This is my bio. For now this is here only for testing purposes so stop reading now, there is nothing interesting here.<\\/p>\",\"pic\":\"https:\\/\\/devbranch.openstack.org\\/profile_images\\/members\\/11624\",\"linked_in\":\"linkedin.com\\/sebastian.marcet\",\"irc\":\"sebastian.irc\",\"twitter\":\"smarcet\",\"feedback\":[{\"id\":1,\"rate\":2,\"note\":\"test\",\"event_id\":6805,\"created_date\":1448478672,\"owner_id\":1},{\"id\":2,\"rate\":4,\"note\":\"Ffffffffffff\",\"event_id\":6807,\"created_date\":1448488029,\"owner_id\":1},{\"id\":3,\"rate\":5,\"note\":\"Malis quando id mel, ea usu dolor malorum, numquam democritum at duo. An quo prompta quaestio. Id vix sonet posidonium. In dicta saepe adolescens est. Id tibique convenire definiebas vix. Ne tritani intellegat mediocritatem his.\",\"event_id\":6805,\"created_date\":1448653212,\"owner_id\":1},{\"id\":4,\"rate\":4,\"note\":\"Meliore singulis id has, ex errem omittantur appellantur ius. Te qui elit pertinax, te reprimique signiferumque per. Sit consequat efficiendi scripserit ad, in quo stet delenit atomorum, vim in novum consequat. Iisque aliquid dignissim eos ea, id nec illud corrumpit. Qui eu idque epicurei nominati.\",\"event_id\":6805,\"created_date\":1448655561,\"owner_id\":1},{\"id\":5,\"rate\":2,\"note\":\"Meliore singulis id has, ex errem omittantur appellantur ius. Te qui elit pertinax, te reprimique signiferumque per. Sit consequat efficiendi scripserit ad, in quo stet delenit atomorum, vim in novum consequat. Iisque aliquid dignissim eos ea, id nec illud corrumpit. Qui eu idque epicurei nominati.Meliore singulis id has, ex errem omittantur appellantur ius. Te qui elit pertinax, te reprimique signiferumque per. Sit consequat efficiendi\",\"event_id\":6805,\"created_date\":1448655662,\"owner_id\":1},{\"id\":6,\"rate\":3,\"note\":\"TESTING FEEDBACK\",\"event_id\":6805,\"created_date\":1448655679,\"owner_id\":1},{\"id\":7,\"rate\":1,\"note\":\"Meliore singulis id has, ex errem omittantur appellantur ius. Te qui elit pertinax, te reprimique signiferumque per. Sit consequat efficiendi scripserit ad, in quo stet delenit atomorum, vim in novum consequat. Iisque aliquid dignissim eos ea, id nec illud corrumpit. Qui eu idque epicurei nominati.\",\"event_id\":6805,\"created_date\":1448660527,\"owner_id\":1},{\"id\":8,\"rate\":5,\"note\":\"Testigo feedback feedbackfe edbackfeedback feedback feedback feedback\",\"event_id\":3874,\"created_date\":1448904646,\"owner_id\":1},{\"id\":9,\"rate\":3,\"note\":\"testing testing testing testing testing testing testing testing teinsfdf ested tesdsd sd d sdsdsd sdsd sd sdadsd\",\"event_id\":4803,\"created_date\":1448906446,\"owner_id\":1},{\"id\":10,\"rate\":4,\"note\":\"This ruled\",\"event_id\":5014,\"created_date\":1448946327,\"owner_id\":1},{\"id\":11,\"rate\":5,\"note\":\"Great job\",\"event_id\":6821,\"created_date\":1449082907,\"owner_id\":1},{\"id\":12,\"rate\":3,\"note\":\"Boo boo\",\"event_id\":6828,\"created_date\":1449083318,\"owner_id\":1},{\"id\":13,\"rate\":4,\"note\":\"Great feedback!\",\"event_id\":6823,\"created_date\":1449167147,\"owner_id\":1},{\"id\":14,\"rate\":5,\"note\":\"Wow\",\"event_id\":6169,\"created_date\":1449352059,\"owner_id\":1},{\"id\":15,\"rate\":3,\"note\":\"Hahaha\",\"event_id\":3659,\"created_date\":1450124954,\"owner_id\":1},{\"id\":16,\"rate\":4,\"note\":\"Love it\",\"event_id\":6821,\"created_date\":1450197125,\"owner_id\":1}]}";
        PresentationSpeakerDeserializer presentationSpeakerDeserializer = mock(PresentationSpeakerDeserializer.class);

        SummitAttendeeDeserializer summitAttendeeDeserializer = mock(SummitAttendeeDeserializer.class);
        PersonDeserializer personDeserializer = mock(PersonDeserializer.class);
        FeedbackDeserializer feedbackDeserializer = mock(FeedbackDeserializer.class);
        SummitAttendee summitAttendee = new SummitAttendee();
        when(summitAttendeeDeserializer.deserialize(eq(jsonString))).thenReturn(summitAttendee);

        IMemberDeserializer memberDeserializer = new MemberDeserializer(personDeserializer, presentationSpeakerDeserializer, summitAttendeeDeserializer, feedbackDeserializer);

        // Act
        Member member = memberDeserializer.deserialize(jsonString);

        // Assert
        Assert.assertEquals(1, member.getId());
        Assert.assertEquals(summitAttendee, member.getAttendeeRole());
        Assert.assertNull(member.getSpeakerRole());
    }
}
