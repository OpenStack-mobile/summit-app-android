package org.openstack.android.openstacksummit.common.data_access.deserialization;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.openstacksummit.common.entities.Feedback;
import org.openstack.android.openstacksummit.common.entities.SummitAttendee;
import org.openstack.android.openstacksummit.common.entities.SummitEvent;
import org.openstack.android.openstacksummit.common.entities.TicketType;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/16/2015.
 */
public class SummitAttendeeDeserializer extends BaseDeserializer implements ISummitAttendeeDeserializer {
    IPersonDeserializer personDeserializer;
    IDeserializerStorage deserializerStorage;
    IDeserializer deserializer;

    @Inject
    public SummitAttendeeDeserializer(IPersonDeserializer personDeserializer, IDeserializerStorage deserializerStorage, IDeserializer deserializer) {
        this.personDeserializer = personDeserializer;
        this.deserializerStorage = deserializerStorage;
        this.deserializer = deserializer;
    }

    @Override
    public SummitAttendee deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[] {"id", "first_name", "last_name", "ticket_type_id"},  jsonObject);
        handleMissedFieldsIfAny(missedFields);

        SummitAttendee summitAttendee = new SummitAttendee();
        personDeserializer.deserialize(summitAttendee, jsonObject);

        SummitEvent summitEvent;
        int summitEventId;
        JSONArray jsonArraySummitEvents = jsonObject.getJSONArray("schedule");
        for (int i = 0; i < jsonArraySummitEvents.length(); i++) {
            summitEventId = jsonArraySummitEvents.getInt(i);
            summitEvent = deserializerStorage.get(summitEventId, SummitEvent.class);
            summitAttendee.getScheduledEvents().add(summitEvent);
        }

        TicketType ticketType = deserializerStorage.get(jsonObject.getInt("ticket_type_id"), TicketType.class);
        summitAttendee.setTicketType(ticketType);

        Feedback feedback;
        JSONObject jsonObjectFeedback;
        JSONArray jsonArrayFeedback = jsonObject.getJSONArray("event_types");
        for (int i = 0; i < jsonArrayFeedback.length(); i++) {
            jsonObjectFeedback = jsonArrayFeedback.getJSONObject(i);
            feedback = deserializer.deserialize(jsonObjectFeedback.toString(), Feedback.class);
            summitAttendee.getFeedback().add(feedback);
        }

        if(!deserializerStorage.exist(summitAttendee, SummitAttendee.class)) {
            deserializerStorage.add(summitAttendee, SummitAttendee.class);
        }

        return summitAttendee;
    }
}
