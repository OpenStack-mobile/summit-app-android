package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.SummitAttendee;
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
        String[] missedFields = validateRequiredFields(new String[] {"id", "rate", "created_date", "event_id"},  jsonObject);
        handleMissedFieldsIfAny(missedFields);

        Feedback feedback = new Feedback();
        feedback.setId(jsonObject.getInt("id"));
        feedback.setRate(jsonObject.getInt("rate"));
        feedback.setReview(
                !jsonObject.isNull("note") ? jsonObject.getString("note") : null
        );
        feedback.setDate(new Date(jsonObject.getInt("created_date")*1000L));

        int ownerId;
        if (jsonObject.has("attendee_id")){
            ownerId  = jsonObject.getInt("attendee_id");
            feedback.setOwner(deserializerStorage.get(ownerId, SummitAttendee.class));
        }
        else if (jsonObject.has("member_id")) {
            int memberId = jsonObject.getInt("member_id");
            Member member = deserializerStorage.get(memberId, Member.class);
            if(member != null && member.getAttendeeRole() != null)
                feedback.setOwner(member.getAttendeeRole());
        }
        else if (jsonObject.has("owner")) {
            SummitAttendee summitAttendee = new SummitAttendee();
            JSONObject jsonObjectAttendee = jsonObject.getJSONObject("owner");
            summitAttendee.setId(jsonObjectAttendee.getInt("id"));
            summitAttendee.setFullName(jsonObjectAttendee.getString("first_name") + " " + jsonObjectAttendee.getString("last_name"));
            feedback.setOwner(summitAttendee);
        }

        if(feedback.getOwner() == null)
            throw new JSONException(String.format("Can't deserialize owner for feedback with id %d", feedback.getId()));

        feedback.setEvent(deserializerStorage.get(jsonObject.getInt("event_id"), SummitEvent.class));

        if(feedback.getEvent() == null)
            throw new JSONException(String.format("Can't deserialize event for feedback with id %d", feedback.getId()));

        if(!deserializerStorage.exist(feedback, Feedback.class)) {
            deserializerStorage.add(feedback, Feedback.class);
        }

        return feedback;
    }
}
