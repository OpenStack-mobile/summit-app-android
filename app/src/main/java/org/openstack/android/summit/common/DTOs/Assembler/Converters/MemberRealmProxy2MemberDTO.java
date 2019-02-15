package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import io.realm.MemberRealmProxy;
import io.realm.SpeakerRealmProxy;
import io.realm.SummitAttendeeRealmProxy;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public class MemberRealmProxy2MemberDTO extends AbstractMember2MemberDTO<MemberRealmProxy, SummitAttendeeRealmProxy, SpeakerRealmProxy> {

    public MemberRealmProxy2MemberDTO() {
        abstractSummitAttendee2PersonDTO = new SummitAttendeeRealmProxy2PersonDTO();
        abstractSpeaker2PersonDTO        = new SpeakerRealmProxy2PersonDTO();
    }
}
