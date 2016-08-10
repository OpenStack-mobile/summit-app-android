package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.IPresentationMaterial;
import org.openstack.android.summit.common.entities.PresentationVideo;

import java.util.Date;

/**
 * Created by sebastian on 8/10/2016.
 */
public class PresentationVideoDeserializer extends PresentationMaterialDeserializer implements IPresentationVideoDeserializer {

    public PresentationVideoDeserializer(IDeserializerStorage deserializerStorage) {
        super(deserializerStorage);
    }

    @Override
    public PresentationVideo deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        String[] missedFields = validateRequiredFields(new String[] {"youtube_id","highlighted","data_uploaded","views"},  jsonObject);
        handleMissedFieldsIfAny(missedFields);
        PresentationVideo video = (PresentationVideo)internalDeserialize(jsonString);
        video.setYouTubeId(jsonObject.getString("youtube_id"));
        video.setHighlighted(jsonObject.getBoolean("highlighted"));
        video.setDateUploaded(new Date(jsonObject.getLong("data_uploaded") * 1000L));
        video.setViews(jsonObject.getLong("views"));
        if(!deserializerStorage.exist(video, PresentationVideo.class)) {
            deserializerStorage.add(video, PresentationVideo.class);
        }
        return video;
    }

    @Override
    protected IPresentationMaterial buildMaterial() {
        return new PresentationVideo();
    }
}
