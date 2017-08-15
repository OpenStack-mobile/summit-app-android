package org.openstack.android.summit.modules.member_profile.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.MemberDTO;
import org.openstack.android.summit.common.DTOs.PersonDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.repositories.IPresentationSpeakerDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;

/**
 * Created by Claudio Redi on 1/26/2016.
 */
public class MemberProfileInteractor extends BaseInteractor implements IMemberProfileInteractor {

    protected IPresentationSpeakerDataStore presentationSpeakerDataStore;

    public MemberProfileInteractor(IPresentationSpeakerDataStore presentationSpeakerDataStore, ISecurityManager securityManager, IDTOAssembler dtoAssembler, ISummitDataStore summitDataStore, ISummitSelector summitSelector, IReachability reachability) {
        super(securityManager, dtoAssembler, summitSelector, summitDataStore, reachability);
        this.presentationSpeakerDataStore = presentationSpeakerDataStore;
    }

    @Override
    public MemberDTO getCurrentMember() {
        Member member = securityManager.getCurrentMember();
        return (member != null) ? dtoAssembler.createDTO(member, MemberDTO.class) : null;
    }

    @Override
    public PersonDTO getPresentationSpeaker(int speakerId) {
        PresentationSpeaker presentationSpeaker = presentationSpeakerDataStore.getById(speakerId);
        return (presentationSpeaker != null) ? dtoAssembler.createDTO(presentationSpeaker, PersonDTO.class) : null;
    }

    @Override
    public boolean isLoggedInAndConfirmedAttendee() {
        return securityManager.isLoggedInAndConfirmedAttendee();
    }
}
