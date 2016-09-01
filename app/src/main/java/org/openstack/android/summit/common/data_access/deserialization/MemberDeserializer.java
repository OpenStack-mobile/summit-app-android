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
import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/13/2015.
 */
public class MemberDeserializer extends BaseDeserializer implements IMemberDeserializer {

    IPersonDeserializer              personDeserializer;
    IPresentationSpeakerDeserializer presentationSpeakerDeserializer;
    ISummitAttendeeDeserializer      summitAttendeeDeserializer;
    IFeedbackDeserializer            feedbackDeserializer;
    IDeserializerStorage             deserializerStorage;

    @Inject
    public MemberDeserializer
            (
                    IPersonDeserializer personDeserializer,
                    IPresentationSpeakerDeserializer presentationSpeakerDeserializer,
                    ISummitAttendeeDeserializer summitAttendeeDeserializer,
                    IFeedbackDeserializer feedbackDeserializer,
                    IDeserializerStorage deserializerStorage
            )
    {
        this.personDeserializer              = personDeserializer;
        this.deserializerStorage             = deserializerStorage;
        this.feedbackDeserializer            = feedbackDeserializer;
        this.presentationSpeakerDeserializer = presentationSpeakerDeserializer;
        this.summitAttendeeDeserializer      = summitAttendeeDeserializer;
    }

    @Override
    public Member deserialize(String jsonString) throws JSONException {

        JSONObject jsonObject = new JSONObject(jsonString);
        String[] missedFields = validateRequiredFields(new String[] {"id"},  jsonObject);
        handleMissedFieldsIfAny(missedFields);
        int memberId = jsonObject.getInt("id");
        Member member = deserializerStorage.exist(memberId, Member.class) ? deserializerStorage.get(memberId, Member.class) : new Member();
        personDeserializer.deserialize(member, jsonObject);

        // added here so it's available on child deserialization
        if(!deserializerStorage.exist(member, Member.class)) {
            deserializerStorage.add(member, Member.class);
        }

        if (jsonObject.has("speaker")) {
            JSONObject speakerJSONObject            = jsonObject.getJSONObject("speaker");
            PresentationSpeaker presentationSpeaker = presentationSpeakerDeserializer.deserialize(speakerJSONObject.toString());
            member.setSpeakerRole(presentationSpeaker);
        }

        if (jsonObject.has("attendee")) {
            JSONObject attendeeJSONObject = jsonObject.getJSONObject("attendee");
            SummitAttendee attendee       = summitAttendeeDeserializer.deserialize(attendeeJSONObject.toString());
            member.setAttendeeRole(attendee);
        }

        if(jsonObject.has("feedback")) {
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

        return member;
    }
}
