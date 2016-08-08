package org.openstack.android.summit.modules.member_profile_detail.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.MemberDTO;
import org.openstack.android.summit.common.DTOs.PersonDTO;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.security.ISecurityManager;

/**
 * Created by Claudio Redi on 1/26/2016.
 */
public class MemberProfileDetailInteractor extends BaseInteractor implements IMemberProfileDetailInteractor {
    protected ISecurityManager securityManager;
    protected IGenericDataStore genericDataStore;

    public MemberProfileDetailInteractor(IGenericDataStore genericDataStore, ISecurityManager securityManager, IDTOAssembler dtoAssembler) {
        super(dtoAssembler);
        this.securityManager = securityManager;
        this.genericDataStore = genericDataStore;
    }
    @Override
    public MemberDTO getCurrentMember() {
        Member member = securityManager.getCurrentMember();
        return (member != null) ? dtoAssembler.createDTO(member, MemberDTO.class) : null;
    }

    @Override
    public PersonDTO getPresentationSpeaker(int speakerId) {
        PresentationSpeaker presentationSpeaker = genericDataStore.getByIdLocal(speakerId, PresentationSpeaker.class);
        return (presentationSpeaker != null) ? dtoAssembler.createDTO(presentationSpeaker, PersonDTO.class):null;
    }
}