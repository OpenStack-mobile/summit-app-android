package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.Presentation;
import org.openstack.android.summit.common.entities.PresentationLink;
import org.openstack.android.summit.common.entities.PresentationSlide;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.PresentationVideo;
import org.openstack.android.summit.common.entities.Track;
import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/13/2015.
 */
public class PresentationDeserializer extends BaseDeserializer implements IPresentationDeserializer {
    IDeserializerStorage deserializerStorage;
    IPresentationSpeakerDeserializer presentationSpeakerDeserializer;
    IPresentationLinkDeserializer presentationLinkDeserializer;
    IPresentationVideoDeserializer presentationVideoDeserializer;
    IPresentationSlideDeserializer presentationSlideDeserializer;

    @Inject
    public PresentationDeserializer
    (
        IPresentationSpeakerDeserializer presentationSpeakerDeserializer,
        IDeserializerStorage deserializerStorage,
        IPresentationLinkDeserializer presentationLinkDeserializer,
        IPresentationVideoDeserializer presentationVideoDeserializer,
        IPresentationSlideDeserializer presentationSlideDeserializer
    ){
        this.deserializerStorage             = deserializerStorage;
        this.presentationSpeakerDeserializer = presentationSpeakerDeserializer;
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
        Presentation presentation = deserializerStorage.exist(presentationId, Presentation.class) ?
                deserializerStorage.get(presentationId, Presentation.class) :
                new Presentation();

        presentation.setId(presentationId);

        if(!deserializerStorage.exist(presentation, Presentation.class)) {
            deserializerStorage.add(presentation, Presentation.class);
        }

        presentation.setLevel(
                !jsonObject.isNull("level") ? jsonObject.getString("level") : null
        );

        int trackId    = jsonObject.getInt("track_id");
        //first check db, and then cache storage
        Track track    = deserializerStorage.get(trackId, Track.class);
        if(track == null)
            throw new JSONException(String.format("Can't deserialize presentation id %d missing track %d", presentationId , trackId));

        presentation.setTrack(track);

        PresentationSpeaker presentationSpeaker;
        int speakerId;
        JSONArray jsonArraySpeakers = jsonObject.getJSONArray("speakers");

        presentation.getSpeakers().clear();
        for (int i = 0; i < jsonArraySpeakers.length(); i++) {
            speakerId = jsonArraySpeakers.optInt(i);
            presentationSpeaker = (speakerId > 0) ?
                    deserializerStorage.get(speakerId, PresentationSpeaker.class) :
                    presentationSpeakerDeserializer.deserialize(jsonArraySpeakers.getJSONObject(i).toString());
            if(presentationSpeaker == null) continue;
            presentation.getSpeakers().add(presentationSpeaker);
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

        if (!jsonObject.isNull("moderator_speaker_id")) {
            presentationSpeaker = deserializerStorage.get(jsonObject.getInt("moderator_speaker_id"), PresentationSpeaker.class);
            presentation.setModerator(presentationSpeaker);
        }

        return presentation;
    }
}
