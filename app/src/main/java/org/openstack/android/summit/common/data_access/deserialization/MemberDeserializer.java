package org.openstack.android.summit.common.data_access.deserialization;

import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.summit.common.entities.IPerson;
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
        member.setFullName(jsonObject.has("name") ? jsonObject.optString("name") : getFullName(jsonObject));
        member.setPictureUrl(jsonObject.optString("picture"));

        if (jsonObject.has("member_id")) {
            member.setId(jsonObject.optInt("member_id"));
            SummitAttendee summitAttendee = summitAttendeeDeserializer.deserialize(jsonString);
            member.setAttendeeRole(summitAttendee);
        }
        else {
            member.setId(jsonObject.optInt("id"));
        }

        if (jsonObject.has("speaker")) {
            JSONObject speakerJSONObject = jsonObject.getJSONObject("speaker");
            PresentationSpeaker presentationSpeaker = presentationSpeakerDeserializer.deserialize(speakerJSONObject.toString());
            member.setSpeakerRole(presentationSpeaker);
        }
        return member;
    }

    private String getFullName(JSONObject jsonObject) {
        String fullName = null;
        String firstName = jsonObject.optString("first_name");
        String lastName = jsonObject.optString("last_name");
        
        if (firstName != null && lastName != null) {
            fullName = firstName + " " + lastName;
        }
        else if (firstName != null){
            fullName = firstName;
        }
        else  if (lastName != null){
            fullName = lastName;
        }
        return fullName;
    }
}
