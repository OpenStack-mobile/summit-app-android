package org.openstack.android.summit.common.DTOs.Assembler.Converters;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.modelmapper.AbstractConverter;
import org.openstack.android.summit.common.Constants;
import org.openstack.android.summit.common.DTOs.MemberDTO;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Speaker;
import org.openstack.android.summit.common.entities.SummitAttendee;

/**
 * Created by Claudio Redi on 1/27/2016.
 */
public class AbstractMember2MemberDTO<S extends Member, A extends SummitAttendee, P extends Speaker>
        extends AbstractConverter<S, MemberDTO> {

    AbstractSummitAttendee2PersonDTO<A> abstractSummitAttendee2PersonDTO;
    AbstractSpeaker2PersonDTO<P> abstractSpeaker2PersonDTO;

    @Override
    protected MemberDTO convert(S source) {

        MemberDTO memberDTO = new MemberDTO();

        try {

            memberDTO.setId(source.getId());
            memberDTO.setFirstName(source.getFirstName());
            memberDTO.setLastName(source.getLastName());
            memberDTO.setName(source.getFirstName() + " " + source.getLastName());
            memberDTO.setPictureUrl(source.getPictureUrl());
            memberDTO.setBio(source.getBio());
            memberDTO.setIrc(source.getIrc());
            memberDTO.setTwitter(source.getTwitter());

            if (source.getAttendeeRole() != null) {
                memberDTO.setAttendeeRole(abstractSummitAttendee2PersonDTO.convert((A)source.getAttendeeRole()));
            }
            if (source.getSpeakerRole() != null) {
                memberDTO.setSpeakerRole(abstractSpeaker2PersonDTO.convert((P)source.getSpeakerRole()));
            }
        }
        catch (Exception e) {
            Crashlytics.logException(e);
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
            throw e;
        }

        return memberDTO;
    }
}
