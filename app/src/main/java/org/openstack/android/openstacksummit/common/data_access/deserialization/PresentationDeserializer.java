package org.openstack.android.openstacksummit.common.data_access.deserialization;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.openstacksummit.common.entities.Presentation;
import org.openstack.android.openstacksummit.common.entities.PresentationSpeaker;
import org.openstack.android.openstacksummit.common.entities.Tag;
import org.openstack.android.openstacksummit.common.entities.Track;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/13/2015.
 */
public class PresentationDeserializer extends BaseDeserializer implements IPresentationDeserializer {
    IDeserializer deserializer;
    IDeserializerStorage deserializerStorage;

    @Inject
    public PresentationDeserializer(IDeserializer deserializer, IDeserializerStorage deserializerStorage){
        this.deserializer = deserializer;
        this.deserializerStorage = deserializerStorage;
    }

    @Override
    public Presentation deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[] {"id"},  jsonObject);
        handleMissedFieldsIfAny(missedFields);

        Presentation presentation = new Presentation();
        presentation.setId(jsonObject.getInt("id"));
        presentation.setLevel(
                !jsonObject.isNull("level") ? jsonObject.getString("level") : null
        );

        Track track = deserializerStorage.get(jsonObject.getInt("track_id"), Track.class);
        presentation.setTrack(track);

        PresentationSpeaker presentationSpeaker;
        int speakerId;
        JSONArray jsonArraySpeakers = jsonObject.getJSONArray("speakers");
        for (int i = 0; i < jsonArraySpeakers.length(); i++) {
            speakerId = jsonArraySpeakers.getInt(i);
            presentationSpeaker = deserializerStorage.get(speakerId, PresentationSpeaker.class);
            presentation.getSpeakers().add(presentationSpeaker);
        }

        return presentation;
    }
}
