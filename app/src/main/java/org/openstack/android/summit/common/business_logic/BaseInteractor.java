package org.openstack.android.summit.common.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.entities.IEntity;
import org.openstack.android.summit.common.entities.Summit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public class BaseInteractor implements IBaseInteractor {

    protected IDTOAssembler dtoAssembler;
    protected ISummitSelector summitSelector;
    protected ISummitDataStore summitDataStore;

    public BaseInteractor
    (
        IDTOAssembler dtoAssembler,
        ISummitSelector summitSelector,
        ISummitDataStore summitDataStore
    ) {
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

}
