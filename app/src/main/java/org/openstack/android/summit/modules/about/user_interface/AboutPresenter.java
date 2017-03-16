package org.openstack.android.summit.modules.about.user_interface;

import android.os.Bundle;

import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.DTOs.WifiListItemDTO;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.common.user_interface.WifiItemView;
import org.openstack.android.summit.modules.about.IAboutWireframe;
import org.openstack.android.summit.modules.about.business_logic.IAboutInteractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio Redi on 4/1/2016.
 */
public class AboutPresenter extends BasePresenter<IAboutView, IAboutInteractor, IAboutWireframe> implements IAboutPresenter {

    private List<WifiListItemDTO> wifiList = new ArrayList<>();

    public AboutPresenter(IAboutInteractor interactor, IAboutWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        SummitDTO summit  = interactor.getActiveSummit();
        wifiList          = interactor.getWifiList();

        view.setSummitName(summit.getName());
        view.setSummitDate(summit.getDatesLabel());
        view.setWifiConnections(wifiList);
    }

    @Override
    public void buildWifiListItem(WifiItemView wifiItemView, int position) {
        WifiListItemDTO wifiListItemDTO = wifiList.get(position);
        wifiItemView.setSsid(wifiListItemDTO.getSsid());
        wifiItemView.setPassword(wifiListItemDTO.getPassword());
    }
}
