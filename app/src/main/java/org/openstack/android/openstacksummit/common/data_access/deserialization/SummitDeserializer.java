package org.openstack.android.openstacksummit.common.data_access.deserialization;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.openstacksummit.common.entities.Company;
import org.openstack.android.openstacksummit.common.entities.EventType;
import org.openstack.android.openstacksummit.common.entities.PresentationSpeaker;
import org.openstack.android.openstacksummit.common.entities.Summit;
import org.openstack.android.openstacksummit.common.entities.SummitEvent;
import org.openstack.android.openstacksummit.common.entities.SummitType;
import org.openstack.android.openstacksummit.common.entities.TicketType;
import org.openstack.android.openstacksummit.common.entities.Venue;
import org.openstack.android.openstacksummit.common.entities.VenueRoom;

import java.util.Date;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/15/2015.
 */
public class SummitDeserializer extends BaseDeserializer implements ISummitDeserializer {
    IDeserializer deserializer;
    IDeserializerStorage deserializerStorage;

    @Inject
    public SummitDeserializer(IDeserializer deserializer, IDeserializerStorage deserializerStorage){
        this.deserializer = deserializer;
        this.deserializerStorage = deserializerStorage;
    }

    @Override
    public Summit deserialize(String jsonString) throws JSONException {
        Summit summit = new Summit();
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[] {"id", "name", "start_date", "end_date", "time_zone", "sponsors", "summit_types", "ticket_types", "event_types", "tracks", "locations", "speakers", "schedule"},  jsonObject);
        if (missedFields.length > 0) {
            throw new JSONException("Following fields are missed " + TextUtils.join(",", missedFields));
        }

        summit.setId(jsonObject.getInt("id"));
        summit.setName(jsonObject.getString("name"));
        summit.setStartDate(new Date(jsonObject.getInt("start_date")));
        summit.setEndDate(new Date(jsonObject.getInt("end_date")));
        summit.setTimeZone(jsonObject.getJSONObject("time_zone").getString("name"));

        Company company;
        JSONObject jsonObjectSponsor;
        JSONArray jsonArraySponsors = jsonObject.getJSONArray("sponsors");
        for (int i = 0; i < jsonArraySponsors.length(); i++) {
            jsonObjectSponsor = jsonArraySponsors.getJSONObject(i);
            company = deserializer.deserialize(jsonObjectSponsor.toString(), Company.class);
            deserializerStorage.add(company, Company.class);
        }

        SummitType summitType;
        JSONObject jsonObjectSummitType;
        JSONArray jsonArraySummitTypes = jsonObject.getJSONArray("summit_types");
        for (int i = 0; i < jsonArraySummitTypes.length(); i++) {
            jsonObjectSummitType = jsonArraySummitTypes.getJSONObject(i);
            summitType = deserializer.deserialize(jsonObjectSummitType.toString(), SummitType.class);
            summit.getTypes().add(summitType);
            deserializerStorage.add(summitType, SummitType.class);
        }

        TicketType ticketType;
        JSONObject jsonObjectTicketType;
        JSONArray jsonArrayTicketTypes = jsonObject.getJSONArray("ticket_types");
        for (int i = 0; i < jsonArrayTicketTypes.length(); i++) {
            jsonObjectTicketType = jsonArrayTicketTypes.getJSONObject(i);
            ticketType = deserializer.deserialize(jsonObjectTicketType.toString(), TicketType.class);
            summit.getTicketTypes().add(ticketType);
            deserializerStorage.add(ticketType, TicketType.class);
        }

        EventType eventType;
        JSONObject jsonObjectEventType;
        JSONArray jsonArrayEventTypes = jsonObject.getJSONArray("event_types");
        for (int i = 0; i < jsonArrayEventTypes.length(); i++) {
            jsonObjectEventType = jsonArrayEventTypes.getJSONObject(i);
            eventType = deserializer.deserialize(jsonObjectEventType.toString(), EventType.class);
            deserializerStorage.add(eventType, EventType.class);
        }

        Venue venue;
        JSONObject jsonObjectVenue;
        JSONArray jsonArrayVenues = jsonObject.getJSONArray("locations");
        for (int i = 0; i < jsonArrayVenues.length(); i++) {
            jsonObjectVenue = jsonArrayVenues.getJSONObject(i);
            if (isVenue(jsonObjectVenue)) {
                venue = deserializer.deserialize(jsonObjectVenue.toString(), Venue.class);
                deserializerStorage.add(venue, Venue.class);
            }
        }

        VenueRoom venueRoom;
        JSONObject jsonObjectVenueRoom;
        JSONArray jsonArrayVenueRooms = jsonObject.getJSONArray("locations");
        for (int i = 0; i < jsonArrayVenueRooms.length(); i++) {
            jsonObjectVenueRoom = jsonArrayVenueRooms.getJSONObject(i);
            if (isVenue(jsonObjectVenueRoom)) {
                venueRoom = deserializer.deserialize(jsonObjectVenueRoom.toString(), VenueRoom.class);
                deserializerStorage.add(venueRoom, VenueRoom.class);
            }
        }

        PresentationSpeaker presentationSpeaker;
        JSONObject jsonObjectPresentationSpeaker;
        JSONArray jsonArrayPresentationSpeakers = jsonObject.getJSONArray("speakers");
        for (int i = 0; i < jsonArrayPresentationSpeakers.length(); i++) {
            jsonObjectPresentationSpeaker = jsonArrayPresentationSpeakers.getJSONObject(i);
            presentationSpeaker = deserializer.deserialize(jsonObjectPresentationSpeaker.toString(), PresentationSpeaker.class);
            deserializerStorage.add(presentationSpeaker, PresentationSpeaker.class);
        }

        SummitEvent summitEvent;
        JSONObject jsonObjectSummitEvent;
        JSONArray jsonArraySummitEvents = jsonObject.getJSONArray("schedule");
        for (int i = 0; i < jsonArraySummitEvents.length(); i++) {
            jsonObjectSummitEvent = jsonArraySummitEvents.getJSONObject(i);
            summitEvent = deserializer.deserialize(jsonObjectSummitEvent.toString(), SummitEvent.class);
            deserializerStorage.add(summitEvent, SummitEvent.class);
            summit.getEvents().add(summitEvent);
        }

        return summit;
    }

    private boolean isVenue(JSONObject jsonObjectVenue) throws JSONException {
        return jsonObjectVenue.getString("class_name") == "SummitVenue";
    }

    private boolean isVenueRoom(JSONObject jsonObjectVenueRoom) throws JSONException {
        return jsonObjectVenueRoom.getString("class_name") == "SummitVenueRoom";
    }
}
