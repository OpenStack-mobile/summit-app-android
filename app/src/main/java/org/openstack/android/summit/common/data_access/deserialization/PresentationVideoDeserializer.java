package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.IPresentationMaterial;
import org.openstack.android.summit.common.entities.PresentationVideo;
import org.openstack.android.summit.common.utils.RealmFactory;

import java.util.Date;

/**
 * Created by sebastian on 8/10/2016.
 */
public class PresentationVideoDeserializer extends PresentationMaterialDeserializer implements IPresentationVideoDeserializer {

    public PresentationVideoDeserializer() {
        super();
    }

    @Override
    public PresentationVideo deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        String[] missedFields = validateRequiredFields(new String[] {"youtube_id","highlighted", "data_uploaded"},  jsonObject);
        handleMissedFieldsIfAny(missedFields);
        PresentationVideo video = (PresentationVideo)internalDeserialize(jsonString);
        video.setYouTubeId(jsonObject.getString("youtube_id"));
        video.setHighlighted(jsonObject.getBoolean("highlighted"));
        video.setDateUploaded(new Date(jsonObject.getLong("data_uploaded") * 1000L));
        video.setViews(jsonObject.isNull("views")? 0 :  jsonObject.getLong("views"));
        return video;
    }

    @Override
    protected IPresentationMaterial buildMaterial(int materialId) {

        PresentationVideo video = RealmFactory.getSession().where(PresentationVideo.class).equalTo("id", materialId).findFirst();
        if(video == null)
            video = RealmFactory.getSession().createObject(PresentationVideo.class);

        return video;
    }
}
