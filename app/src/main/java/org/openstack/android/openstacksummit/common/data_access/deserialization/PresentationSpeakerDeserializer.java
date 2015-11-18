package org.openstack.android.openstacksummit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.openstacksummit.common.entities.PresentationSpeaker;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/13/2015.
 */
public class PresentationSpeakerDeserializer extends BaseDeserializer implements IPresentationSpeakerDeserializer {
    IPersonDeserializer personDeserializer;

    @Inject
    public PresentationSpeakerDeserializer(IPersonDeserializer personDeserializer) {
        this.personDeserializer = personDeserializer;
    }

    @Override
    public PresentationSpeaker deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[] {"id", "first_name", "last_name"},  jsonObject);
        handleMissedFieldsIfAny(missedFields);

        PresentationSpeaker presentationSpeaker = new PresentationSpeaker();
        personDeserializer.deserialize(presentationSpeaker, jsonObject);
        return presentationSpeaker;
    }
}
