package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.IPresentationMaterial;
import org.openstack.android.summit.common.entities.Presentation;
import org.openstack.android.summit.common.entities.PresentationSpeaker;

import javax.inject.Inject;

/**
 * Created by sebastian on 8/10/2016.
 */
abstract public class PresentationMaterialDeserializer extends BaseDeserializer {

    IDeserializerStorage deserializerStorage;

    public PresentationMaterialDeserializer(IDeserializerStorage deserializerStorage) {
        this.deserializerStorage = deserializerStorage;
    }

    protected IPresentationMaterial internalDeserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[] {"id","display_on_site","featured", "presentation_id"},  jsonObject);
        handleMissedFieldsIfAny(missedFields);

        IPresentationMaterial material = buildMaterial();
        material.setId(jsonObject.getInt("id"));
        material.setName(jsonObject.isNull("order") ? null : jsonObject.getString("name"));
        material.setDescription(!jsonObject.isNull("description")?jsonObject.getString("description"):null);
        material.setDisplayOnSite(jsonObject.getBoolean("display_on_site"));
        material.setFeatured(jsonObject.getBoolean("featured"));
        material.setOrder(jsonObject.isNull("order") ? 0 :  jsonObject.getInt("order"));

        return material;
    }

    abstract protected IPresentationMaterial buildMaterial();
}
