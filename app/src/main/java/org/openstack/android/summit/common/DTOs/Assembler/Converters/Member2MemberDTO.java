package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.SummitAttendee;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public class Member2MemberDTO extends AbstractMember2MemberDTO<Member, SummitAttendee, PresentationSpeaker> {

    public Member2MemberDTO() {
        abstractSummitAttendee2PersonDTO      = new SummitAttendee2PersonDTO();
        abstractPresentationSpeaker2PersonDTO = new PresentationSpeaker2PersonDTO();
    }
}
