package org.openstack.android.summit.common.data_access.deserialization;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.SummitGroupEvent;
import org.openstack.android.summit.common.utils.RealmFactory;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/13/2015.
 */
public class MemberDeserializer extends BaseDeserializer implements IMemberDeserializer {

    IPersonDeserializer personDeserializer;
    IPresentationSpeakerDeserializer presentationSpeakerDeserializer;
    ISummitAttendeeDeserializer summitAttendeeDeserializer;
    IFeedbackDeserializer feedbackDeserializer;
    ISummitEventDeserializer eventDeserializer;


    @Inject
    public MemberDeserializer
    (
        IPersonDeserializer personDeserializer,
        IPresentationSpeakerDeserializer presentationSpeakerDeserializer,
        ISummitAttendeeDeserializer summitAttendeeDeserializer,
        IFeedbackDeserializer feedbackDeserializer,
        ISummitEventDeserializer eventDeserializer
    ) {
        this.personDeserializer              = personDeserializer;
        this.feedbackDeserializer            = feedbackDeserializer;
        this.presentationSpeakerDeserializer = presentationSpeakerDeserializer;
        this.summitAttendeeDeserializer      = summitAttendeeDeserializer;
        this.eventDeserializer               = eventDeserializer;
    }

    @Override
    public Member deserialize(String jsonString) throws JSONException {

        JSONObject jsonObject = new JSONObject(jsonString);
        String[] missedFields = validateRequiredFields(new String[]{"id"}, jsonObject);
        handleMissedFieldsIfAny(missedFields);
        int memberId = jsonObject.getInt("id");

        Member member = RealmFactory.getSession().where(Member.class).equalTo("id", memberId).findFirst();
        if(member == null)
            member = RealmFactory.getSession().createObject(Member.class, memberId);

        personDeserializer.deserialize(member, jsonObject);

        member.setSpeakerRole(null);
        if (jsonObject.has("speaker")) {
            JSONObject speakerJSONObject = jsonObject.getJSONObject("speaker");
            PresentationSpeaker presentationSpeaker = presentationSpeakerDeserializer.deserialize(speakerJSONObject.toString());
            member.setSpeakerRole(presentationSpeaker);
        }

        member.setAttendeeRole(null);
        if (jsonObject.has("attendee")) {
            JSONObject attendeeJSONObject = jsonObject.getJSONObject("attendee");
            SummitAttendee attendee = summitAttendeeDeserializer.deserialize(attendeeJSONObject.toString());
            member.setAttendeeRole(attendee);
        }

        if (jsonObject.has("feedback")) {
            Feedback feedback;
            JSONObject jsonObjectFeedback;
            JSONArray jsonArrayFeedback = jsonObject.getJSONArray("feedback");
            member.clearFeedback();
            for (int i = 0; i < jsonArrayFeedback.length(); i++) {
                jsonObjectFeedback = jsonArrayFeedback.getJSONObject(i);
                try {
                    feedback = feedbackDeserializer.deserialize(jsonObjectFeedback.toString());
                    member.getFeedback().add(feedback);
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Log.e(Constants.LOG_TAG, String.format("Error deserializing feedback %s", jsonObjectFeedback.toString()), e);
                }
            }
        }

        if (jsonObject.has("groups_events")) {
            JSONArray jsonArrayEventGroups = jsonObject.getJSONArray("groups_events");
            JSONObject jsonObjectGroupEvent;
            SummitEvent event;
            member.clearGroupEvents();
            for (int i = 0; i < jsonArrayEventGroups.length(); i++) {
                jsonObjectGroupEvent = jsonArrayEventGroups.getJSONObject(i);
                try {
                    event = eventDeserializer.deserialize(jsonObjectGroupEvent.toString());
                    member.addGroupEvent(event.getGroupEvent());
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Log.e(Constants.LOG_TAG, String.format("Error deserializing group event %s", jsonObjectGroupEvent.toString()), e);
                }
            }
        }

        return member;
    }
}
