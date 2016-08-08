package org.openstack.android.summit.common.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.entities.IEntity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public class BaseInteractor implements IBaseInteractor {

    protected IDTOAssembler dtoAssembler;

    public BaseInteractor(IDTOAssembler dtoAssembler) {
        this.dtoAssembler = dtoAssembler;
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

}
