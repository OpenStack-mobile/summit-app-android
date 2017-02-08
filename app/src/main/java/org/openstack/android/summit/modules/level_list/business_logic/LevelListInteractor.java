package org.openstack.android.summit.modules.level_list.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.data_access.repositories.ISummitEventDataStore;
import org.openstack.android.summit.common.security.ISecurityManager;

import java.util.List;
import javax.inject.Inject;

/**
 * Created by Claudio Redi on 1/11/2016.
 */
public class LevelListInteractor extends BaseInteractor implements ILevelListInteractor {

    ISummitEventDataStore summitEventDataStore;

    @Inject
    public LevelListInteractor
    (
        ISecurityManager securityManager,
        ISummitEventDataStore summitEventDataStore,
        IDTOAssembler dtoAssembler,
        ISummitDataStore summitDataStore,
        ISummitSelector summitSelector
    )
    {
        super(securityManager, dtoAssembler, summitSelector, summitDataStore);
        this.summitEventDataStore = summitEventDataStore;
    }

    @Override
    public List<String> getLevels() {
        return summitEventDataStore.getPresentationLevels();
    }
}
