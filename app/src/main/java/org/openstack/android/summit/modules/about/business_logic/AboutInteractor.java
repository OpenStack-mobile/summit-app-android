package org.openstack.android.summit.modules.about.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.security.ISecurityManager;

/**
 * Created by Claudio Redi on 4/2/2016.
 */
public class AboutInteractor extends BaseInteractor implements IAboutInteractor {

    public AboutInteractor(ISecurityManager securityManager, ISummitDataStore summitDataStore, IDTOAssembler dtoAssembler, ISummitSelector summitSelector) {
        super(securityManager, dtoAssembler, summitSelector, summitDataStore);
        this.summitDataStore = summitDataStore;
    }

}
