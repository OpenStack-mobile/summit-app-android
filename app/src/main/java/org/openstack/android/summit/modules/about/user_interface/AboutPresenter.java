package org.openstack.android.summit.modules.about.user_interface;

import android.content.pm.PackageInfo;
import android.os.Bundle;

import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.DTOs.WifiListItemDTO;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.common.user_interface.WifiItemView;
import org.openstack.android.summit.modules.about.IAboutWireframe;
import org.openstack.android.summit.modules.about.business_logic.IAboutInteractor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import static org.openstack.android.summit.R.string.event;

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
        SummitDTO summit = interactor.getActiveSummit();
        String summitDate = buildDate(summit);
        wifiList = interactor.getWifiList();

        view.setSummitName(summit.getName());
        view.setSummitDate(summitDate);
        view.setWifiConnections(wifiList);
    }

    private String buildDate(SummitDTO summit) {
        DateFormat formatterFrom = new SimpleDateFormat("MMMM dd-");
        formatterFrom.setTimeZone(TimeZone.getTimeZone(summit.getTimeZone()));

        DateFormat formatterTo = new SimpleDateFormat("dd, yyyy");
        formatterTo.setTimeZone(TimeZone.getTimeZone(summit.getTimeZone()));

        String timeRange = String.format("%s%s", formatterFrom.format(summit.getStartDate()), formatterTo.format(summit.getEndDate()));

        return timeRange;
    }

    @Override
    public void buildWifiListItem(WifiItemView wifiItemView, int position) {
        WifiListItemDTO wifiListItemDTO = wifiList.get(position);
        wifiItemView.setSsid(wifiListItemDTO.getSsid());
        wifiItemView.setPassword(wifiListItemDTO.getPassword());
    }
}
