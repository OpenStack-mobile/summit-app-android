package org.openstack.android.openstacksummit;

import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.utils.RealmFactory;
import org.openstack.android.summit.common.utils.Void;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Claudio Redi on 11/17/2015.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ModelTests extends InstrumentationTestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Realm.setDefaultConfiguration( new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .inMemory()
                .build());
    }

    @Test
    public void memberOverwritingTest(){
        try {
            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception {
                    Summit summit = new Summit();
                    summit.setId(1);
                    summit.setName("test summit");

                    summit = session.copyToRealmOrUpdate(summit);

                    SummitEvent event = new SummitEvent();
                    event.setId(1);
                    event.setName("test event");
                    event.setSummit(summit);

                    event = session.copyToRealmOrUpdate(event);

                    return Void.getInstance();
                }
            });

            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception {
                    Member member = new Member();
                    member.setId(1);
                    member.setFirstName("Sebastian");
                    member.setLastName("Marcet");
                    member = session.copyToRealmOrUpdate(member);
                    SummitEvent event = session.where(SummitEvent.class).equalTo("id", 1).findFirst();
                    Feedback feedback1 = new Feedback();
                    feedback1.setId(1);
                    feedback1.setRate(1);
                    feedback1.setReview("test");
                    feedback1.setOwner(member);
                    feedback1.setEvent(event);
                    feedback1 = session.copyToRealmOrUpdate(feedback1);
                    member.getFeedback().add(feedback1);
                    return Void.getInstance();
                }
            });

            RealmFactory.transaction(new RealmFactory.IRealmCallback<Void>() {
                @Override
                public Void callback(Realm session) throws Exception {
                    Member member = new Member();
                    member.setId(1);
                    member.setFirstName("Sebastian");
                    member.setLastName("Marcet");
                    member = session.copyToRealmOrUpdate(member);
                    SummitEvent event = session.where(SummitEvent.class).equalTo("id", 1).findFirst();
                    Feedback feedback1 = new Feedback();
                    feedback1.setId(1);
                    feedback1.setRate(1);
                    feedback1.setReview("test");
                    feedback1.setOwner(member);
                    feedback1.setEvent(event);
                    feedback1 = session.copyToRealmOrUpdate(feedback1);
                    member.getFeedback().add(feedback1);
                    return Void.getInstance();
                }
            });

        }
        catch (Exception ex){

        }

        Member member = RealmFactory.getSession().where(Member.class).equalTo("id", 1).findFirst();

        Assert.assertTrue(member.getFeedback().size() > 0);
    }


    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
