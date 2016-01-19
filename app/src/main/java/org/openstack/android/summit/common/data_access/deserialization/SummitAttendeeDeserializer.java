package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.Feedback;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.TicketType;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/16/2015.
 */
public class SummitAttendeeDeserializer extends BaseDeserializer implements ISummitAttendeeDeserializer {
    IPersonDeserializer personDeserializer;
    IDeserializerStorage deserializerStorage;
    IFeedbackDeserializer feedbackDeserializer;

    @Inject
    public SummitAttendeeDeserializer(IPersonDeserializer personDeserializer, IFeedbackDeserializer feedbackDeserializer, IDeserializerStorage deserializerStorage) {
        this.personDeserializer = personDeserializer;
        this.deserializerStorage = deserializerStorage;
        this.feedbackDeserializer = feedbackDeserializer;
    }

    @Override
    public SummitAttendee deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[] {"id", "first_name", "last_name", "tickets"},  jsonObject);
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

        TicketType ticketType;
        int ticketTypeId;
        JSONArray jsonArrayTicketTypes = jsonObject.getJSONArray("tickets");
        for (int i = 0; i < jsonArrayTicketTypes.length(); i++) {
            ticketTypeId = jsonArrayTicketTypes.getInt(i);
            ticketType = deserializerStorage.get(ticketTypeId, TicketType.class);
            summitAttendee.getTicketTypes().add(ticketType);
        }

        Feedback feedback;
        JSONObject jsonObjectFeedback;
        JSONArray jsonArrayFeedback = jsonObject.getJSONArray("feedback");
        for (int i = 0; i < jsonArrayFeedback.length(); i++) {
            jsonObjectFeedback = jsonArrayFeedback.getJSONObject(i);
            feedback = feedbackDeserializer.deserialize(jsonObjectFeedback.toString());
            summitAttendee.getFeedback().add(feedback);
        }

        if(!deserializerStorage.exist(summitAttendee, SummitAttendee.class)) {
            deserializerStorage.add(summitAttendee, SummitAttendee.class);
        }

        return summitAttendee;
    }
}
