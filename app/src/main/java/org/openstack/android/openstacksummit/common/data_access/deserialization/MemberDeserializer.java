package org.openstack.android.openstacksummit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.openstacksummit.common.entities.Member;
import org.openstack.android.openstacksummit.common.entities.PresentationSpeaker;
import org.openstack.android.openstacksummit.common.entities.SummitAttendee;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/13/2015.
 */
public class MemberDeserializer implements IMemberDeserializer {
    IDeserializer deserializer;

    @Inject
    public MemberDeserializer(IDeserializer deserializer) {
        this.deserializer = deserializer;
    }

    @Override
    public Member deserialize(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        Member member = new Member();
        member.setId(jsonObject.getInt("id"));
        SummitAttendee summitAttendee = deserializer.deserialize(jsonObject.getJSONObject("attendee").toString(), SummitAttendee.class);
        PresentationSpeaker presentationSpeaker = deserializer.deserialize(jsonObject.getJSONObject("speaker").toString(), PresentationSpeaker.class);
        member.setAttendeeRole(summitAttendee);
        member.setSkeaperRole(presentationSpeaker);
        return member;
    }
}
