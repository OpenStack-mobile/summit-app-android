package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.util.Date;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/12/2015.
 */
public class FeedbackDeserializer extends BaseDeserializer implements IFeedbackDeserializer {

    @Inject
    public FeedbackDeserializer() {
    }

    @Override
    public Feedback deserialize(String jsonString) throws JSONException {

        JSONObject jsonObject = new JSONObject(jsonString);
        String[] missedFields = validateRequiredFields(new String[]{"id", "rate", "created_date", "event_id"}, jsonObject);
        handleMissedFieldsIfAny(missedFields);
        int feedbackId    = jsonObject.getInt("id");

        Feedback feedback = RealmFactory.getSession().where(Feedback.class).equalTo("id", feedbackId).findFirst();
        if(feedback == null)
            feedback = RealmFactory.getSession().createObject(Feedback.class);

        feedback.setId(jsonObject.getInt("id"));
        feedback.setRate(jsonObject.getInt("rate"));
        feedback.setReview(
                !jsonObject.isNull("note") ? jsonObject.getString("note") : null
        );

        feedback.setDate(new Date(jsonObject.getInt("created_date") * 1000L));

        if (jsonObject.has("member_id")) {
            int memberId = jsonObject.getInt("member_id");
            Member member =  RealmFactory.getSession().where(Member.class).equalTo("id", memberId).findFirst();
            if (member != null)
                feedback.setOwner(member);
        } else if (jsonObject.has("owner")) {
            Member member = RealmFactory.getSession().createObject(Member.class);
            JSONObject jsonObjectAttendee = jsonObject.getJSONObject("owner");
            member.setId(jsonObjectAttendee.getInt("id"));
            member.setFullName(jsonObjectAttendee.getString("first_name") + " " + jsonObjectAttendee.getString("last_name"));
            feedback.setOwner(member);
        }

        if (feedback.getOwner() == null)
            throw new JSONException(String.format("Can't deserialize owner for feedback with id %d", feedback.getId()));

        int eventId       = jsonObject.getInt("event_id");
        SummitEvent event = RealmFactory.getSession().where(SummitEvent.class).equalTo("id", eventId).findFirst();

        feedback.setEvent(event);

        if (feedback.getEvent() == null)
            throw new JSONException(String.format("Can't deserialize event for feedback with id %d", feedback.getId()));

        return feedback;

    }
}
