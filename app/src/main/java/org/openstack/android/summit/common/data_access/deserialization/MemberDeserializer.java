package org.openstack.android.summit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.SummitAttendee;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/13/2015.
 */
public class MemberDeserializer implements IMemberDeserializer {
    IPresentationSpeakerDeserializer presentationSpeakerDeserializer;
    ISummitAttendeeDeserializer summitAttendeeDeserializer;

    @Inject
    public MemberDeserializer(IPresentationSpeakerDeserializer presentationSpeakerDeserializer, ISummitAttendeeDeserializer summitAttendeeDeserializer) {
        this.presentationSpeakerDeserializer = presentationSpeakerDeserializer;
        this.summitAttendeeDeserializer = summitAttendeeDeserializer;
    }

    @Override
    public Member deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        Member member = new Member();
        member.setId(jsonObject.getInt("id"));
        SummitAttendee summitAttendee = summitAttendeeDeserializer.deserialize(jsonString);
        member.setAttendeeRole(summitAttendee);

        if (jsonObject.has("speaker")) {
            JSONObject speakerJSONObject = jsonObject.getJSONObject("speaker");
            PresentationSpeaker presentationSpeaker = presentationSpeakerDeserializer.deserialize(speakerJSONObject.toString());
            member.setSpeakerRole(presentationSpeaker);
        }
        return member;
    }
}
