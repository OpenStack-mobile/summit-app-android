package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.Organization;
import org.openstack.android.summit.common.entities.Speaker;
import org.openstack.android.summit.common.utils.RealmFactory;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/13/2015.
 */
public class SpeakerDeserializer extends BaseDeserializer implements ISpeakerDeserializer {
    IPersonDeserializer personDeserializer;

    @Inject
    public SpeakerDeserializer(IPersonDeserializer personDeserializer) {
        this.personDeserializer  = personDeserializer;
    }

    @Override
    public Speaker deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[]{"id"}, jsonObject);
        handleMissedFieldsIfAny(missedFields);
        int speakerId = jsonObject.getInt("id");

        Speaker speaker = RealmFactory.getSession().where(Speaker.class).equalTo("id", speakerId).findFirst();
        if(speaker == null)
            speaker = RealmFactory.getSession().createObject(Speaker.class, speakerId);

        personDeserializer.deserialize(speaker, jsonObject);

        if(jsonObject.has("affiliations")){
            JSONArray jsonAffiliations = jsonObject.getJSONArray("affiliations");
            speaker.clearAffiliations();
            for (int i = 0; i < jsonAffiliations.length(); i++){
                JSONObject jsonAffiliation = jsonAffiliations.getJSONObject(i);
                JSONObject jsonOrg         = jsonAffiliation.getJSONObject("organization");
                int orgId                  = jsonOrg.getInt("id");
                String orgName             = jsonOrg.getString("name");

                Organization org = RealmFactory.getSession().where(Organization.class).equalTo("id", orgId).findFirst();
                if(org == null)
                    org = RealmFactory.getSession().createObject(Organization.class, orgId);
                org.setName(orgName);
                speaker.getAffiliations().add(org);
            }
        }

        return speaker;
    }
}
