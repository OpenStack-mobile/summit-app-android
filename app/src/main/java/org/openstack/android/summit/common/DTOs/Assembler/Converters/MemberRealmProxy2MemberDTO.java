package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.SummitAttendee;

import io.realm.MemberRealmProxy;
import io.realm.PresentationSpeakerRealmProxy;
import io.realm.SummitAttendeeRealmProxy;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public class MemberRealmProxy2MemberDTO extends AbstractMember2MemberDTO<MemberRealmProxy, SummitAttendeeRealmProxy, PresentationSpeakerRealmProxy> {

    public MemberRealmProxy2MemberDTO() {
        abstractSummitAttendee2PersonDTO      = new SummitAttendeeRealmProxy2PersonDTO();
        abstractPresentationSpeaker2PersonDTO = new PresentationSpeakerRealmProxy2PersonDTO();
    }
}
