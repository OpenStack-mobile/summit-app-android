package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.IPresentationMaterial;
import org.openstack.android.summit.common.entities.PresentationSlide;

/**
 * Created by sebastian on 8/10/2016.
 */
public class PresentationSlideDeserializer extends PresentationMaterialDeserializer implements IPresentationSlideDeserializer {

    public PresentationSlideDeserializer(IDeserializerStorage deserializerStorage) {
        super(deserializerStorage);
    }

    @Override
    public PresentationSlide deserialize(String jsonString) throws JSONException {

        PresentationSlide slide = (PresentationSlide)internalDeserialize(jsonString);

        JSONObject jsonObject = new JSONObject(jsonString);
        String[] missedFields = validateRequiredFields(new String[] {"link"},  jsonObject);
        handleMissedFieldsIfAny(missedFields);

        slide.setLink(jsonObject.getString("link"));
        if(!deserializerStorage.exist(slide, PresentationSlide.class)) {
            deserializerStorage.add(slide, PresentationSlide.class);
        }
        return slide;
    }

    @Override
    protected IPresentationMaterial buildMaterial() {
        return new PresentationSlide();
    }
}
