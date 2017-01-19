package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.utils.RealmFactory;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/13/2015.
 */
public class PresentationSpeakerDeserializer extends BaseDeserializer implements IPresentationSpeakerDeserializer {
    IPersonDeserializer personDeserializer;

    @Inject
    public PresentationSpeakerDeserializer(IPersonDeserializer personDeserializer) {
        this.personDeserializer  = personDeserializer;
    }

    @Override
    public PresentationSpeaker deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[]{"id"}, jsonObject);
        handleMissedFieldsIfAny(missedFields);
        int speakerId = jsonObject.getInt("id");

        PresentationSpeaker speaker = RealmFactory.getSession().where(PresentationSpeaker.class).equalTo("id", speakerId).findFirst();
        if(speaker == null)
            speaker = RealmFactory.getSession().createObject(PresentationSpeaker.class, speakerId);

        personDeserializer.deserialize(speaker, jsonObject);

        return speaker;
    }
}
