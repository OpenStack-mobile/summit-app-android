package org.openstack.android.summit.common.business_logic;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.MemberDTO;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.entities.IEntity;
import org.openstack.android.summit.common.entities.Member;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.network.IReachability;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public class BaseInteractor implements IBaseInteractor {

    protected IDTOAssembler  dtoAssembler;
    protected ISummitSelector summitSelector;
    protected ISummitDataStore summitDataStore;
    protected ISecurityManager securityManager;
    protected IReachability reachability;

    public BaseInteractor
    (
        ISecurityManager securityManager,
        IDTOAssembler dtoAssembler,
        ISummitSelector summitSelector,
        ISummitDataStore summitDataStore,
        IReachability reachability
    )
    {
        this.securityManager = securityManager;
        this.reachability    = reachability;
        this.dtoAssembler    = dtoAssembler;
        this.summitDataStore = summitDataStore;
        this.summitSelector  = summitSelector;
    }

    protected <S extends IEntity, D> List<D> createDTOList(List<S> sourceList, Class<D> destinationType) {
        ArrayList<D> dtos = new ArrayList<>();
        D dto;
        for (S entity: sourceList) {
            dto = dtoAssembler.createDTO(entity, destinationType);
            dtos.add(dto);
        }

        return dtos;
    }

    protected <S extends IEntity, D> D createDTO(S source, Class<D> destinationType) {
       return  dtoAssembler.createDTO(source, destinationType);
    }

    @Override
    public boolean isDataLoaded() {
        Summit summit = summitDataStore.getById(summitSelector.getCurrentSummitId());
        return summit != null && summit.isScheduleLoaded();
    }

    @Override
    public SummitDTO getActiveSummit() {
        Summit summit = summitDataStore.getById(summitSelector.getCurrentSummitId());
        if(summit == null) return null;
        return dtoAssembler.createDTO(summit, SummitDTO.class);
    }

    @Override
    public SummitDTO getLatestSummit(){
        Summit summit = summitDataStore.getLatest();
        if(summit == null) return null;
        return dtoAssembler.createDTO(summit, SummitDTO.class);
    }

    @Override
    public boolean isMemberLoggedIn() {
        return securityManager.isLoggedIn();
    }

    @Override
    public boolean isMemberLoggedInAndConfirmedAttendee() {
        return securityManager.isLoggedInAndConfirmedAttendee();
    }

    @Override
    public MemberDTO getCurrentMember() {
        Member member = securityManager.getCurrentMember();
        if(member == null) return null;
        return dtoAssembler.createDTO(member, MemberDTO.class);
    }

    @Override
    public boolean isNetworkingAvailable(){
        return reachability.isNetworkingAvailable(OpenStackSummitApplication.context);
    }

}
