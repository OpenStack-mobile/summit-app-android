package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.IPresentationMaterial;
import org.openstack.android.summit.common.entities.PresentationLink;
import org.openstack.android.summit.common.utils.RealmFactory;

/**
 * Created by sebastian on 8/10/2016.
 */
public class PresentationLinkDeserializer extends PresentationMaterialDeserializer implements IPresentationLinkDeserializer {

    public PresentationLinkDeserializer() {
        super();
    }

    @Override
    public PresentationLink deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        String[] missedFields = validateRequiredFields(new String[]{"link"}, jsonObject);
        handleMissedFieldsIfAny(missedFields);
        PresentationLink link = (PresentationLink) internalDeserialize(jsonString);
        link.setLink(jsonObject.getString("link"));
        return link;
    }

    @Override
    protected IPresentationMaterial buildMaterial(int materialId) {

        PresentationLink link = RealmFactory.getSession().where(PresentationLink.class).equalTo("id", materialId).findFirst();
        if(link == null)
            link = RealmFactory.getSession().createObject(PresentationLink.class, materialId);

        return link;
    }
}
