package org.openstack.android.summit.modules.about.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;

/**
 * Created by Claudio Redi on 4/2/2016.
 */
public class AboutInteractor extends BaseInteractor implements IAboutInteractor {

    public AboutInteractor(ISummitDataStore summitDataStore, IDTOAssembler dtoAssembler, ISummitSelector summitSelector) {
        super(dtoAssembler, summitSelector, summitDataStore);
        this.summitDataStore = summitDataStore;
    }

}
