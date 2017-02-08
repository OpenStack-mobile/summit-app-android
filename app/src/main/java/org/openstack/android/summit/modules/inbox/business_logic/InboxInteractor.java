package org.openstack.android.summit.modules.inbox.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.security.ISecurityManager;

/**
 * Created by smarcet on 2/7/17.
 */

public class InboxInteractor extends BaseInteractor implements IInboxInteractor {

    public InboxInteractor
    (
        ISecurityManager securityManager,
        IDTOAssembler dtoAssembler,
        ISummitSelector summitSelector,
        ISummitDataStore summitDataStore
    ) {
        super(securityManager, dtoAssembler, summitSelector, summitDataStore);
    }
}
