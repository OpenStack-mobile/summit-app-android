package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.IPresentationMaterial;
import org.openstack.android.summit.common.entities.PresentationLink;

/**
 * Created by sebastian on 8/10/2016.
 */
public class PresentationLinkDeserializer extends PresentationMaterialDeserializer implements IPresentationLinkDeserializer {

    public PresentationLinkDeserializer(IDeserializerStorage deserializerStorage) {
        super(deserializerStorage);
    }

    @Override
    public PresentationLink deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        String[] missedFields = validateRequiredFields(new String[]{"link"}, jsonObject);
        handleMissedFieldsIfAny(missedFields);
        PresentationLink link = (PresentationLink) internalDeserialize(jsonString);
        link.setLink(jsonObject.getString("link"));
        if (!deserializerStorage.exist(link, PresentationLink.class)) {
            deserializerStorage.add(link, PresentationLink.class);
        }
        return link;
    }

    @Override
    protected IPresentationMaterial buildMaterial(int materialId) {
        return deserializerStorage.exist(materialId, PresentationLink.class) ?
                deserializerStorage.get(materialId, PresentationLink.class) :
                new PresentationLink();
    }
}
