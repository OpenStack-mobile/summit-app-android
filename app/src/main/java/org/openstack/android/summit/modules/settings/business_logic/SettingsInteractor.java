package org.openstack.android.summit.modules.settings.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.business_logic.BaseInteractor;

/**
 * Created by sebastian on 9/19/2016.
 */
public class SettingsInteractor extends BaseInteractor implements ISettingsInteractor {

    public SettingsInteractor(IDTOAssembler dtoAssembler) {
        super(dtoAssembler);
    }
}
