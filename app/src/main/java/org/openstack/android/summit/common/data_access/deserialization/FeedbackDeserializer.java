package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.SummitEvent;

import java.util.Date;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/12/2015.
 */
public class FeedbackDeserializer extends BaseDeserializer implements IFeedbackDeserializer {
    IDeserializerStorage deserializerStorage;

    @Inject
    public FeedbackDeserializer(IDeserializerStorage deserializerStorage) {
        this.deserializerStorage = deserializerStorage;
    }

    @Override
    public Feedback deserialize(String jsonString) throws JSONException {

        JSONObject jsonObject = new JSONObject(jsonString);
        String[] missedFields = validateRequiredFields(new String[]{"id", "rate", "created_date", "event_id"}, jsonObject);
        handleMissedFieldsIfAny(missedFields);
        int feedbackId = jsonObject.getInt("id");
        Feedback feedback = deserializerStorage.exist(feedbackId, Feedback.class) ? deserializerStorage.get(feedbackId, Feedback.class) : new Feedback();
        feedback.setId(jsonObject.getInt("id"));
        feedback.setRate(jsonObject.getInt("rate"));
        feedback.setReview(
                !jsonObject.isNull("note") ? jsonObject.getString("note") : null
        );
        feedback.setDate(new Date(jsonObject.getInt("created_date") * 1000L));

        if (jsonObject.has("member_id")) {
            int memberId = jsonObject.getInt("member_id");
            Member member = deserializerStorage.get(memberId, Member.class);
            if (member != null)
                feedback.setOwner(member);
        } else if (jsonObject.has("owner")) {
            Member member = new Member();
            JSONObject jsonObjectAttendee = jsonObject.getJSONObject("owner");
            member.setId(jsonObjectAttendee.getInt("id"));
            member.setFullName(jsonObjectAttendee.getString("first_name") + " " + jsonObjectAttendee.getString("last_name"));
            feedback.setOwner(member);
        }

        if (feedback.getOwner() == null)
            throw new JSONException(String.format("Can't deserialize owner for feedback with id %d", feedback.getId()));

        feedback.setEvent(deserializerStorage.get(jsonObject.getInt("event_id"), SummitEvent.class));

        if (feedback.getEvent() == null)
            throw new JSONException(String.format("Can't deserialize event for feedback with id %d", feedback.getId()));

        if (!deserializerStorage.exist(feedback, Feedback.class)) {
            deserializerStorage.add(feedback, Feedback.class);
        }

        return feedback;

    }
}
