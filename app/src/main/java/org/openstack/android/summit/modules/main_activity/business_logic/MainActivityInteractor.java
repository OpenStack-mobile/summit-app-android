package org.openstack.android.summit.modules.main_activity.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.security.ISecurityManagerListener;

/**
 * Created by Claudio Redi on 2/12/2016.
 */
public class MainActivityInteractor extends BaseInteractor implements IMainActivityInteractor {
    ISecurityManager securityManager;

    public MainActivityInteractor(ISecurityManager securityManager, IDTOAssembler dtoAssembler) {
        super(dtoAssembler);
        this.securityManager = securityManager;
    }

    public void login() {

    }
}
