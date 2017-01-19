package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.IPresentationMaterial;
import org.openstack.android.summit.common.entities.PresentationSlide;
import org.openstack.android.summit.common.utils.RealmFactory;

/**
 * Created by sebastian on 8/10/2016.
 */
public class PresentationSlideDeserializer extends PresentationMaterialDeserializer implements IPresentationSlideDeserializer {

    public PresentationSlideDeserializer() {
        super();
    }

    @Override
    public PresentationSlide deserialize(String jsonString) throws JSONException {

        PresentationSlide slide = (PresentationSlide)internalDeserialize(jsonString);

        JSONObject jsonObject = new JSONObject(jsonString);
        String[] missedFields = validateRequiredFields(new String[] {"link"},  jsonObject);
        handleMissedFieldsIfAny(missedFields);

        slide.setLink(jsonObject.getString("link"));
        return slide;
    }

    @Override
    protected IPresentationMaterial buildMaterial(int materialId) {
        PresentationSlide slide = RealmFactory.getSession().where(PresentationSlide.class).equalTo("id", materialId).findFirst();
        if(slide == null)
            slide = RealmFactory.getSession().createObject(PresentationSlide.class, materialId);
        return slide;
    }
}
