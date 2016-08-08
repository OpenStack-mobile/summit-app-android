package org.openstack.android.summit.modules.about.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.ISummitDataStore;
import org.openstack.android.summit.common.entities.Summit;

/**
 * Created by Claudio Redi on 4/2/2016.
 */
public class AboutInteractor extends BaseInteractor implements IAboutInteractor {
    ISummitDataStore summitDataStore;

    public AboutInteractor(ISummitDataStore summitDataStore, IDTOAssembler dtoAssembler) {
        super(dtoAssembler);
        this.summitDataStore = summitDataStore;
    }

    public SummitDTO getActiveSummit() {
        Summit summit = summitDataStore.getActiveLocal();
        SummitDTO dto = dtoAssembler.createDTO(summit, SummitDTO.class);
        return dto;
    }
}
