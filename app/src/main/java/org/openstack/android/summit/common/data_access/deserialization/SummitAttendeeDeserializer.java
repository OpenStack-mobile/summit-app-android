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
import org.openstack.android.summit.common.utils.RealmFactory;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/16/2015.
 */
public class SummitAttendeeDeserializer extends BaseDeserializer implements ISummitAttendeeDeserializer {


    @Inject
    public SummitAttendeeDeserializer() {

    }

    @Override
    public SummitAttendee deserialize(String jsonString) throws JSONException {

        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[]{"id"}, jsonObject);
        handleMissedFieldsIfAny(missedFields);
        int attendeeId                = jsonObject.getInt("id");


        SummitAttendee attendee = RealmFactory.getSession().where(SummitAttendee.class).equalTo("id", attendeeId).findFirst();
        if(attendee == null)
            attendee = RealmFactory.getSession().createObject(SummitAttendee.class, attendeeId);

        SummitEvent summitEvent;
        int summitEventId = 0;
        JSONArray jsonArraySummitEvents = jsonObject.getJSONArray("schedule");

        attendee.getScheduledEvents().clear();

        for (int i = 0; i < jsonArraySummitEvents.length(); i++) {
            try {
                summitEventId = jsonArraySummitEvents.getInt(i);
                summitEvent   = RealmFactory.getSession().where(SummitEvent.class).equalTo("id", summitEventId).findFirst();
                if (summitEvent != null) {
                    attendee.getScheduledEvents().add(summitEvent);
                }
            } catch (Exception e) {
                Crashlytics.logException(e);
                Log.e(Constants.LOG_TAG, String.format("Error deserializing schedule event %s", summitEventId), e);
            }
        }

        TicketType ticketType;
        int ticketTypeId;
        JSONArray jsonArrayTicketTypes = jsonObject.getJSONArray("tickets");

        attendee.getTicketTypes().clear();

        for (int i = 0; i < jsonArrayTicketTypes.length(); i++) {
            ticketTypeId = jsonArrayTicketTypes.getInt(i);
            ticketType   = RealmFactory.getSession().where(TicketType.class).equalTo("id", ticketTypeId).findFirst();
            attendee.getTicketTypes().add(ticketType);
        }

        return attendee;
    }
}
