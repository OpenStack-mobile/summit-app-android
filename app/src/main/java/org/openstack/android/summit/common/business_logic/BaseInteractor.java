package org.openstack.android.summit.common.business_logic;

import android.content.Context;

import org.openstack.android.summit.OpenStackSummitApplication;
import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.data_access.data_polling.IDataUpdatePoller;
import org.openstack.android.summit.common.entities.IEntity;
import org.openstack.android.summit.common.network.IReachability;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public class BaseInteractor implements IBaseInteractor {

    @Inject
    IReachability reachability;

    @Inject
    IDataUpdatePoller dataUpdatePoller;

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

    public boolean isNetworkingAvailable() {
        return true;//reachability.isNetworkingAvailable(OpenStackSummitApplication.context);
    }

    @Override
    public void stopPolling() {
        if (dataUpdatePoller != null) {
            dataUpdatePoller.stop();
        }
    }

    @Override
    public void startPollingIfNotPollingAlready() {
        if (dataUpdatePoller != null) {
            dataUpdatePoller.startPollingIfNotPollingAlready();
        }
    }
}
