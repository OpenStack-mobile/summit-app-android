package org.openstack.android.summit.modules.member_profile_detail.business_logic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.MemberDTO;
import org.openstack.android.summit.common.DTOs.PersonDTO;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.IGenericDataStore;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.PresentationSpeaker;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.user_interface.BaseFragment;

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
        MemberDTO dto = dtoAssembler.createDTO(member, MemberDTO.class);
        return dto;
    }

    @Override
    public PersonDTO getPresentationSpeaker(int speakerId) {
        PresentationSpeaker presentationSpeaker = genericDataStore.getByIdLocal(speakerId, PresentationSpeaker.class);
        PersonDTO dto = dtoAssembler.createDTO(presentationSpeaker, PersonDTO.class);
        return dto;
    }
}