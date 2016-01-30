package org.openstack.android.summit.modules.member_profile.business_logic;

import org.openstack.android.summit.common.DTOs.MemberDTO;
import org.openstack.android.summit.common.DTOs.PersonDTO;

/**
 * Created by Claudio Redi on 1/26/2016.
 */
public interface IMemberProfileInteractor {
    MemberDTO getCurrentMember();

    PersonDTO getPresentationSpeaker(int speakerId);
}
