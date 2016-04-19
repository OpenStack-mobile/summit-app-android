package org.openstack.android.summit.common.data_access.deserialization;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.entities.Company;
import org.openstack.android.summit.common.entities.EventType;
import org.openstack.android.summit.common.entities.Presentation;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitEvent;
import org.openstack.android.summit.common.entities.SummitType;
import org.openstack.android.summit.common.entities.Tag;
import org.openstack.android.summit.common.entities.Venue;
import org.openstack.android.summit.common.entities.VenueRoom;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/13/2015.
 */
public class SummitEventDeserializer extends BaseDeserializer implements ISummitEventDeserializer {
    IGenericDeserializer genericDeserializer;
    IPresentationDeserializer presentationDeserializer;
    IDeserializerStorage deserializerStorage;


    @Inject
    public SummitEventDeserializer(IGenericDeserializer genericDeserializer, IPresentationDeserializer presentationDeserializer, IDeserializerStorage deserializerStorage){
        this.genericDeserializer = genericDeserializer;
        this.presentationDeserializer = presentationDeserializer;
        this.deserializerStorage = deserializerStorage;
    }

    @Override
    public SummitEvent deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[] {"id"},  jsonObject);
        handleMissedFieldsIfAny(missedFields);

        SummitEvent summitEvent = new SummitEvent();
        summitEvent.setId(jsonObject.optInt("id"));
        summitEvent.setName(jsonObject.optString("title"));
        summitEvent.setAllowFeedback(jsonObject.optBoolean("allow_feedback"));
        summitEvent.setStart(new Date(jsonObject.optLong("start_date") * 1000L));
        summitEvent.setEnd(new Date(jsonObject.optLong("end_date") * 1000L));
        summitEvent.setEventDescription(!jsonObject.isNull("description") ? jsonObject.getString("description") : "");
        summitEvent.setAverageRate(!jsonObject.isNull("avg_feedback_rate") ? jsonObject.getDouble("avg_feedback_rate") : 0);

        if (jsonObject.has("type_id")) {
            EventType eventType = deserializerStorage.get(jsonObject.getInt("type_id"), EventType.class);
            summitEvent.setEventType(eventType);
        }

        if (jsonObject.has("summit_types")) {
            SummitType summitType = null;
            int summitTypeId;
            JSONArray jsonArraySummitTypes = jsonObject.getJSONArray("summit_types");
            for (int i = 0; i < jsonArraySummitTypes.length(); i++) {
                summitTypeId = jsonArraySummitTypes.getInt(i);
                try {
                    summitType = deserializerStorage.get(summitTypeId, SummitType.class);
                    summitEvent.getSummitTypes().add(summitType);
                }
                catch (Exception e) {
                    Crashlytics.setInt("summitTypeId", summitTypeId);
                    Crashlytics.setBool("isSummitTypeNull", summitType == null);
                    Crashlytics.logException(e);
                    throw e;
                }
            }
        }

        if (jsonObject.has("sponsors")) {
            Company company;
            int sponsorId;
            JSONArray jsonArraySponsors = jsonObject.getJSONArray("sponsors");
            for (int i = 0; i < jsonArraySponsors.length(); i++) {

                sponsorId = jsonArraySponsors.optInt(i);
                if (sponsorId > 0) {
                    company = deserializerStorage.get(sponsorId, Company.class);
                } else {
                    company = genericDeserializer.deserialize(jsonArraySponsors.getJSONObject(i).toString(), Company.class);
                }
                summitEvent.getSponsors().add(company);
            }
        }

        if (jsonObject.has("location_id") && !jsonObject.isNull("location_id")) {
            int locationId = jsonObject.getInt("location_id");
            Venue venue = new Venue();
            venue.setId(locationId);
            VenueRoom venueRoom = new VenueRoom();
            venueRoom.setId(locationId);
            if (deserializerStorage.exist(locationId, Venue.class)){
                venue = deserializerStorage.get(locationId, Venue.class);
                summitEvent.setVenue(venue);
            }
            else if (deserializerStorage.exist(locationId, VenueRoom.class)) {
                venueRoom = deserializerStorage.get(locationId, VenueRoom.class);
                summitEvent.setVenueRoom(venueRoom);
            }
        }

        if (jsonObject.has("track_id") && !jsonObject.isNull("track_id")) {
            Presentation presentation = presentationDeserializer.deserialize(jsonString);
            summitEvent.setPresentation(presentation);

            presentation.setSummitEvent(summitEvent);
        }

        if (jsonObject.has("tags")) {
            Tag tag;
            JSONObject jsonObjectTag;
            JSONArray jsonArrayTags = jsonObject.getJSONArray("tags");
            for (int i = 0; i < jsonArrayTags.length(); i++) {
                jsonObjectTag = jsonArrayTags.getJSONObject(i);
                tag = genericDeserializer.deserialize(jsonObjectTag.toString(), Tag.class);
                summitEvent.getTags().add(tag);
            }
        }

        List<Summit> summitList = deserializerStorage.getAll(Summit.class);
        summitEvent.setSummit(summitList.get(0));

        deserializerStorage.add(summitEvent, SummitEvent.class);

        return summitEvent;
    }
}