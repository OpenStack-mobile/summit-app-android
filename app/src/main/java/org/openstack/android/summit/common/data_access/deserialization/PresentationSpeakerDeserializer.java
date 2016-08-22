package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.PresentationSpeaker;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/13/2015.
 */
public class PresentationSpeakerDeserializer extends BaseDeserializer implements IPresentationSpeakerDeserializer {
    IPersonDeserializer personDeserializer;
    IDeserializerStorage deserializerStorage;

    @Inject
    public PresentationSpeakerDeserializer(IPersonDeserializer personDeserializer, IDeserializerStorage deserializerStorage) {
        this.personDeserializer = personDeserializer;
        this.deserializerStorage = deserializerStorage;
    }

    @Override
    public PresentationSpeaker deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[] {"id"},  jsonObject);
        handleMissedFieldsIfAny(missedFields);

        PresentationSpeaker presentationSpeaker = new PresentationSpeaker();
        personDeserializer.deserialize(presentationSpeaker, jsonObject);

        if(!deserializerStorage.exist(presentationSpeaker, PresentationSpeaker.class)) {
            deserializerStorage.add(presentationSpeaker, PresentationSpeaker.class);
        }

        return presentationSpeaker;
    }
}
