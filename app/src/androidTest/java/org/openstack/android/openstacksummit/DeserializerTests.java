package org.openstack.android.openstacksummit;

import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openstack.android.summit.common.data_access.deserialization.FeedbackDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.GenericDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.SummitEventWithFileDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.SummitGroupEventDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.IMemberDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.MemberDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PersonDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PresentationDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PresentationLinkDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PresentationSlideDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PresentationSpeakerDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.PresentationVideoDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.SummitAttendeeDeserializer;
import org.openstack.android.summit.common.data_access.deserialization.SummitEventDeserializer;
import org.openstack.android.summit.common.entities.EventType;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import io.realm.Realm;
import io.realm.RealmConfiguration;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DeserializerTests  extends InstrumentationTestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Realm.setDefaultConfiguration( new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .inMemory()
                .build());

        // create summit and event

        RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
            @Override
            public Void callback(Realm session) throws Exception {
                Summit summit = new Summit();
                summit.setId(1);
                summit.setName("test summit");

                SummitEvent event = new SummitEvent();
                event.setId(1);
                event.setName("test event");
                event.setSummit(summit);
                summit.getEvents().add(event);

                SummitEvent event2 = new SummitEvent();
                event2.setId(2);
                event2.setName("test event2");
                event2.setSummit(summit);
                summit.getEvents().add(event2);

                summit  = session.copyToRealmOrUpdate(summit);

                return Void.getInstance();
            }
        });
    }


    @Test
    public void createHierarchyTest(){
        try {
            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception {
                    final Summit summit       = session.where(Summit.class).equalTo("id", 1).findFirst();

                    RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                        @Override
                        public Void callback(Realm session) throws Exception {

                            SummitEvent event = session.createObject(SummitEvent.class);
                            event.setId(3);

                            SummitEvent event2 = session.where(SummitEvent.class).equalTo("id", 3).findFirst();

                            Assert.assertTrue(event2 != null);
                            Assert.assertTrue(event2.getId() == event.getId());
                            return Void.getInstance();
                        }
                    });
                    return Void.getInstance();
                }
            });

            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception {
                    SummitEvent event1  = session.where(SummitEvent.class).equalTo("id", 1).findFirst();

                    Assert.assertTrue(event1.getName().equals("test event update"));
                    return Void.getInstance();
                }
            });
        }
        catch (Exception ex){

        }
    }

    @Test
    public void basicDeserializeRealm(){
        try {
            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception {
                    String jsonString = "{ \"id\":1, \"name\":\"test\"}";
                    EventType eventType1 = session.createObjectFromJson(EventType.class, jsonString);

                    EventType eventType2 = session.where(EventType.class).equalTo("id", 1).findFirst();
                    return Void.getInstance();
                }
            });

        }
        catch (Exception ex){

        }
    }

    @Test
    public void updateEventTest(){
        try {
            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception {
                    final Summit summit       = session.where(Summit.class).equalTo("id", 1).findFirst();

                    RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                        @Override
                        public Void callback(Realm session) throws Exception {
                            SummitEvent event1  = session.where(SummitEvent.class).equalTo("id", 1).findFirst();
                            event1.setSummit(summit);
                            event1.setName("test event update");
                            return Void.getInstance();
                        }
                    });
                    return Void.getInstance();
                }
            });

            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception {
                    SummitEvent event1  = session.where(SummitEvent.class).equalTo("id", 1).findFirst();

                    Assert.assertTrue(event1.getName().equals("test event update"));
                    return Void.getInstance();
                }
            });
        }
        catch (Exception ex){

        }
    }

    @Test
    public void sameSummitObjectTest(){
        try {
            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception {
                    Summit summit       = session.where(Summit.class).equalTo("id", 1).findFirst();
                    SummitEvent event1  = summit.getEvents().get(0);
                    event1  = session.where(SummitEvent.class).equalTo("id", 1).findFirst();
                    Summit summit1      = event1.getSummit();
                    SummitEvent event2  = session.where(SummitEvent.class).equalTo("id", 2).findFirst();
                    Summit summit2      = event2.getSummit();
                    return Void.getInstance();
                }
            });
        }
        catch (Exception ex){

        }
    }

    @Test
    public void memberOverwritingTest(){

        final String json = "{\"id\":13867,\"first_name\":\"Sebastian\",\"last_name\":\"Marcet\",\"gender\":\"Male\",\"bio\":null,\"linked_in\":\"http:\\/\\/www.linkedin.com\\/in\\/smarcet\",\"irc\":null,\"twitter\":null,\"pic\":\"https:\\/\\/devbranch.openstack.org\\/profile_images\\/members\\/13867\",\"attendee\":{\"id\":5720,\"summit_hall_checked_in\":false,\"summit_hall_checked_in_date\":null,\"shared_contact_info\":false,\"member_id\":13867,\"schedule\":[],\"tickets\":[]},\"feedback\":[{\"id\":147,\"rate\":5,\"note\":\"\\\"great presentation! test feedback.  \\\\n\\\"\",\"created_date\":1472161995,\"event_id\":1,\"member_id\":13867,\"attendee_id\":5720}]}";
        try {
            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception {

                    SummitAttendeeDeserializer summitAttendeeDeserializer = new SummitAttendeeDeserializer();
                    PersonDeserializer personDeserializer = new PersonDeserializer();
                    FeedbackDeserializer feedbackDeserializer = new FeedbackDeserializer();
                    PresentationSpeakerDeserializer presentationSpeakerDeserializer = new PresentationSpeakerDeserializer(personDeserializer);

                    SummitEventDeserializer summitEventDeserializer = new SummitEventDeserializer(new GenericDeserializer(), new PresentationDeserializer(
                            new PresentationSpeakerDeserializer(new PersonDeserializer()),
                            new PresentationLinkDeserializer(),
                            new PresentationVideoDeserializer(),
                            new PresentationSlideDeserializer()
                    ), new SummitGroupEventDeserializer(), new SummitEventWithFileDeserializer());
                    IMemberDeserializer memberDeserializer = new MemberDeserializer(personDeserializer, presentationSpeakerDeserializer, summitAttendeeDeserializer, feedbackDeserializer, summitEventDeserializer);


                    Member member = memberDeserializer.deserialize(json);
                    member       = session.copyToRealmOrUpdate(member);
                    return Void.getInstance();
                }
            });

            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception {

                    SummitAttendeeDeserializer summitAttendeeDeserializer = new SummitAttendeeDeserializer();
                    PersonDeserializer personDeserializer = new PersonDeserializer();
                    FeedbackDeserializer feedbackDeserializer = new FeedbackDeserializer();
                    PresentationSpeakerDeserializer presentationSpeakerDeserializer = new PresentationSpeakerDeserializer(personDeserializer);

                    SummitEventDeserializer summitEventDeserializer = new SummitEventDeserializer(new GenericDeserializer(), new PresentationDeserializer(
                            new PresentationSpeakerDeserializer(new PersonDeserializer()),
                            new PresentationLinkDeserializer(),
                            new PresentationVideoDeserializer(),
                            new PresentationSlideDeserializer()
                    ), new SummitGroupEventDeserializer(), new SummitEventWithFileDeserializer());

                    IMemberDeserializer memberDeserializer = new MemberDeserializer(personDeserializer, presentationSpeakerDeserializer, summitAttendeeDeserializer, feedbackDeserializer, summitEventDeserializer);


                    Member member = memberDeserializer.deserialize(json);
                    member       = session.copyToRealmOrUpdate(member);
                    return Void.getInstance();
                }
            });

        }
        catch (Exception ex){

        }

        Member member = RealmFactory.getSession().where(Member.class).equalTo("id", 13867).findFirst();

        Assert.assertTrue(member.getFeedback().size() > 0);
    }


    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
