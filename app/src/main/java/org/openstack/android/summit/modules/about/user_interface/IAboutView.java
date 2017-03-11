package org.openstack.android.summit.modules.about.user_interface;

import org.openstack.android.summit.common.DTOs.WifiListItemDTO;
import org.openstack.android.summit.common.user_interface.IBaseView;

import java.util.List;

/**
 * Created by Claudio Redi on 4/1/2016.
 */
public interface IAboutView extends IBaseView {
    void setSummitName(String name);

    void setSummitDate(String date);

    void setVersion(String version);

    void setBuild(String build);

    void setWifiConnections(List<WifiListItemDTO> wifiConnections);
}
