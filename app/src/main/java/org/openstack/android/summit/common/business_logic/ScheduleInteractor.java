package org.openstack.android.summit.common.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.data_access.IDataStoreOperationListener;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.entities.Summit;

import javax.inject.Inject;

/**
 * Created by Claudio Redi on 11/18/2015.
 */
public class ScheduleInteractor implements IScheduleInteractor, IDataStoreOperationListener<Summit> {
    private ISummitDataStore summitDataStore;
    private IInteractorOperationListener<SummitDTO> delegate;
    private IDTOAssembler dtoAssembler;

    @Inject
    public ScheduleInteractor(ISummitDataStore summitDataStore, IDTOAssembler dtoAssembler) {
        this.summitDataStore = summitDataStore;
        this.summitDataStore.setDelegate(this);
        this.dtoAssembler = dtoAssembler;
    }

    @Override
    public void onSuceedWithData(Summit data) {
        if (delegate != null) {
            SummitDTO summitDTO = dtoAssembler.createDTO(data, SummitDTO.class);
            delegate.onSuceedWithData(summitDTO);
        }
    }

    @Override
    public void onSucceed() {

    }

    @Override
    public void onError(String message) {
        if (delegate != null) {
            delegate.onError(message);
        }
    }

    @Override
    public IInteractorOperationListener<SummitDTO> getDelegate() {
        return delegate;
    }

    @Override
    public void setDelegate(IInteractorOperationListener<SummitDTO> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void getActiveSummit() {
        summitDataStore.getActive();
    }

}
