package org.openstack.android.summit.modules.main.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;

/**
 * Created by smarcet on 12/9/16.
 */

public class DataLoadingInteractor extends BaseInteractor implements IDataLoadingInteractor {

    public DataLoadingInteractor(IDTOAssembler dtoAssembler, ISummitSelector summitSelector, ISummitDataStore summitDataStore) {
        super(dtoAssembler, summitSelector, summitDataStore);
    }
}
