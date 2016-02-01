package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.Feedback;
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
    public FeedbackDeserializer(IDeserializerStorage deserializerStorage)
    {
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

        int ownwerId;
        if (jsonObject.has("atendee_id")){
            ownwerId = jsonObject.getInt("atendee_id");
        }
        else {
            ownwerId = jsonObject.getInt("owner_id");
        }

        feedback.setOwner(deserializerStorage.get(ownwerId, SummitAttendee.class));
        feedback.setEvent(deserializerStorage.get(jsonObject.getInt("event_id"), SummitEvent.class));

        if(!deserializerStorage.exist(feedback, Feedback.class)) {
            deserializerStorage.add(feedback, Feedback.class);
        }

        return feedback;
    }
}
