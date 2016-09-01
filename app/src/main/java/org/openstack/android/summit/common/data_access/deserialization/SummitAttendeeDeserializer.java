package org.openstack.android.summit.common.data_access.deserialization;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.entities.SummitAttendee;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.TicketType;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/16/2015.
 */
public class SummitAttendeeDeserializer extends BaseDeserializer implements ISummitAttendeeDeserializer {
    IDeserializerStorage deserializerStorage;

    @Inject
    public SummitAttendeeDeserializer(IDeserializerStorage deserializerStorage) {
        this.deserializerStorage = deserializerStorage;
    }

    @Override
    public SummitAttendee deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[] {"id"},  jsonObject);
        handleMissedFieldsIfAny(missedFields);
        int attendeeId = jsonObject.getInt("id");
        SummitAttendee summitAttendee = deserializerStorage.exist(attendeeId, SummitAttendee.class) ? deserializerStorage.get(attendeeId, SummitAttendee.class) : new SummitAttendee();
        summitAttendee.setId(attendeeId);

        // added here so it's available on child deserialization
        if(!deserializerStorage.exist(summitAttendee, SummitAttendee.class)) {
            deserializerStorage.add(summitAttendee, SummitAttendee.class);
        }

        SummitEvent summitEvent;
        int summitEventId = 0;
        JSONArray jsonArraySummitEvents = jsonObject.getJSONArray("schedule");
        summitAttendee.getScheduledEvents().clear();
        for (int i = 0; i < jsonArraySummitEvents.length(); i++) {
            try{
                summitEventId = jsonArraySummitEvents.getInt(i);
                summitEvent = deserializerStorage.get(summitEventId, SummitEvent.class);
                if (summitEvent != null) {
                    summitAttendee.getScheduledEvents().add(summitEvent);
                }
            }
            catch (Exception e) {
                Crashlytics.logException(e);
                Log.e(Constants.LOG_TAG, String.format("Error deserializing schedule event %s", summitEventId), e);
            }
        }

        TicketType ticketType;
        int ticketTypeId;
        JSONArray jsonArrayTicketTypes = jsonObject.getJSONArray("tickets");
        summitAttendee.getTicketTypes().clear();
        for (int i = 0; i < jsonArrayTicketTypes.length(); i++) {
            ticketTypeId = jsonArrayTicketTypes.getInt(i);
            ticketType = deserializerStorage.get(ticketTypeId, TicketType.class);
            summitAttendee.getTicketTypes().add(ticketType);
        }

        return summitAttendee;
    }
}
