package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.Presentation;
import org.openstack.android.summit.common.entities.PresentationLink;
import org.openstack.android.summit.common.entities.PresentationSlide;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.Speaker;
import org.openstack.android.summit.common.entities.PresentationVideo;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.utils.RealmFactory;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/13/2015.
 */
public class PresentationDeserializer extends BaseDeserializer implements IPresentationDeserializer {

    ISpeakerDeserializer speakerDeserializer;
    IPresentationLinkDeserializer    presentationLinkDeserializer;
    IPresentationVideoDeserializer   presentationVideoDeserializer;
    IPresentationSlideDeserializer   presentationSlideDeserializer;

    @Inject
    public PresentationDeserializer
    (
        ISpeakerDeserializer speakerDeserializer,
        IPresentationLinkDeserializer presentationLinkDeserializer,
        IPresentationVideoDeserializer presentationVideoDeserializer,
        IPresentationSlideDeserializer presentationSlideDeserializer
    ){
        this.speakerDeserializer             = speakerDeserializer;
        this.presentationLinkDeserializer    = presentationLinkDeserializer;
        this.presentationVideoDeserializer   = presentationVideoDeserializer;
        this.presentationSlideDeserializer   = presentationSlideDeserializer;
    }

    @Override
    public Presentation deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        String[] missedFields = validateRequiredFields(new String[] {"id"},  jsonObject);
        handleMissedFieldsIfAny(missedFields);
        int presentationId = jsonObject.getInt("id");

        Presentation presentation = RealmFactory.getSession().where(Presentation.class).equalTo("id", presentationId).findFirst();
        if(presentation == null)
            presentation = RealmFactory.getSession().createObject(Presentation.class, presentationId);

        presentation.setLevel(
                !jsonObject.isNull("level") ? jsonObject.getString("level") : null
        );

        presentation.setToRecord(jsonObject.optBoolean("to_record"));

        int summitId            = jsonObject.optInt("summit_id");

        //first check db, and then cache storage
        Summit summit  =  RealmFactory.getSession().where(Summit.class).equalTo("id", summitId).findFirst();

        if(summit == null)
            throw new JSONException(String.format("Can't deserialize presentation id %d (summit not found)!", presentationId));


        if(jsonObject.has("speakers")) {
            Speaker speaker;
            int speakerId;
            String speakerRole;

            JSONArray jsonArraySpeakers = jsonObject.getJSONArray("speakers");

            presentation.getSpeakers().clear();
            for (int i = 0; i < jsonArraySpeakers.length(); i++) {

                JSONObject jsonSpeaker = jsonArraySpeakers.getJSONObject(i);
                if(jsonSpeaker == null) continue;
                missedFields = validateRequiredFields(new String[] {"id", "role"},  jsonSpeaker);

                if(missedFields.length > 0) continue;

                speakerId   = jsonSpeaker.getInt("id");
                speakerRole = jsonSpeaker.getString("role");

                speaker = RealmFactory.getSession().where(Speaker.class).equalTo("id", speakerId).findFirst();
                if(speaker == null)
                    speaker = speakerDeserializer.deserialize(jsonSpeaker.toString());
                if (speaker == null) continue;
                presentation.getSpeakers().add(new PresentationSpeaker(speaker, speakerRole));

                if(summit.getSpeakers().where().equalTo("id", speaker.getId()).count() == 0)
                    summit.getSpeakers().add(speaker);
            }
        }

        if(jsonObject.has("slides")){
            presentation.getSlides().clear();
            JSONArray slides = jsonObject.getJSONArray("slides");
            JSONObject jsonObjSlide  = null;
            PresentationSlide slide  = null;
            for (int i = 0; i < slides.length(); i++) {
                jsonObjSlide = slides.getJSONObject(i);
                slide        = presentationSlideDeserializer.deserialize(jsonObjSlide.toString());
                if(slide == null) continue;
                presentation.getSlides().add(slide);
                slide.setPresentation(presentation);
            }
        }

        if(jsonObject.has("videos")){
            presentation.getVideos().clear();
            JSONArray videos = jsonObject.getJSONArray("videos");
            JSONObject jsonObjVideo  = null;
            PresentationVideo video  = null;
            for (int i = 0; i < videos.length(); i++) {
                jsonObjVideo = videos.getJSONObject(i);
                video        = presentationVideoDeserializer.deserialize(jsonObjVideo.toString());
                if(video == null) continue;
                presentation.getVideos().add(video);
                video.setPresentation(presentation);
            }
        }

        if(jsonObject.has("links")){
            presentation.getLinks().clear();
            JSONArray links = jsonObject.getJSONArray("links");
            JSONObject jsonObjLink = null;
            PresentationLink link  = null;
            for (int i = 0; i < links.length(); i++) {
                jsonObjLink = links.getJSONObject(i);
                link        = presentationLinkDeserializer.deserialize(jsonObjLink.toString());
                if(link == null) continue;
                presentation.getLinks().add(link);
                link.setPresentation(presentation);
            }
        }

        return presentation;
    }
}
