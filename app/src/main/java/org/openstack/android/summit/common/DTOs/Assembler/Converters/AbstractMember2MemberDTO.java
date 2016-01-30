package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import org.modelmapper.AbstractConverter;
import org.openstack.android.summit.common.DTOs.MemberDTO;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.entities.SummitAttendee;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public class AbstractMember2MemberDTO<S extends Member, A extends SummitAttendee, P extends PresentationSpeaker> extends AbstractConverter<S, MemberDTO> {

    AbstractSummitAttendee2PersonDTO<A> abstractSummitAttendee2PersonDTO;
    AbstractPresentationSpeaker2PersonDTO<P> abstractPresentationSpeaker2PersonDTO;

    @Override
    protected MemberDTO convert(S source) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(source.getId());
        if (source.getAttendeeRole() != null) {
            memberDTO.setAttendeeRole(abstractSummitAttendee2PersonDTO.convert((A)source.getAttendeeRole()));
        }
        if (source.getSpeakerRole() != null) {
            memberDTO.setSpeakerRole(abstractPresentationSpeaker2PersonDTO.convert((P)source.getSpeakerRole()));
        }
        return memberDTO;
    }
}
