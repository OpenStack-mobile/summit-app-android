package org.openstack.android.summit.modules.about.business_logic;

import org.openstack.android.summit.common.DTOs.Assembler.IDTOAssembler;
import org.openstack.android.summit.common.DTOs.WifiListItemDTO;
import org.openstack.android.summit.common.api.ISummitSelector;
import org.openstack.android.summit.common.business_logic.BaseInteractor;
import org.openstack.android.summit.common.data_access.repositories.ISummitDataStore;
import org.openstack.android.summit.common.security.ISecurityManager;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.entities.SummitWIFIConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 4/2/2016.
 */
public class AboutInteractor extends BaseInteractor implements IAboutInteractor {

    public AboutInteractor(ISecurityManager securityManager, ISummitDataStore summitDataStore, IDTOAssembler dtoAssembler, ISummitSelector summitSelector) {
        super(securityManager, dtoAssembler, summitSelector, summitDataStore);
        this.summitDataStore = summitDataStore;
    }

    @Override
    public List<WifiListItemDTO> getWifiList() {
        Summit summit = summitDataStore.getById(summitSelector.getCurrentSummitId());
        List<WifiListItemDTO> wifiListItemDTOList = new ArrayList<WifiListItemDTO>();

        if (summit != null) {
            WifiListItemDTO wifiListItemDTO;
            for (SummitWIFIConnection wifiConnection : summit.getWifiConnections()) {
                wifiListItemDTO = dtoAssembler.createDTO(wifiConnection, WifiListItemDTO.class);
                wifiListItemDTOList.add(wifiListItemDTO);
            }
        }
        return wifiListItemDTOList;
    }

}
