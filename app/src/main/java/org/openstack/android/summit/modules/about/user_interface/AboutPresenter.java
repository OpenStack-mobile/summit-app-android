package org.openstack.android.summit.modules.about.user_interface;

import android.content.Intent;
import android.net.Uri;
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
public final class AboutPresenter extends BasePresenter<IAboutView, IAboutInteractor, IAboutWireframe> implements IAboutPresenter {

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
        if (!summit.isCurrentDateTimeInsideSummitRange()) {
            view.hideWifiConnections();
        }
    }

    @Override
    public void buildWifiListItem(WifiItemView wifiItemView, int position) {
        WifiListItemDTO wifiListItemDTO = wifiList.get(position);
        wifiItemView.setSsid(wifiListItemDTO.getSsid());
        wifiItemView.setPassword(wifiListItemDTO.getPassword());
    }

    @Override
    public void redirect2IssueTrackerPage() {
        Uri uri = Uri.parse("https://github.com/OpenStack-mobile/summit-app-android/issues");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        view.startActivity(intent);
    }

    @Override
    public void redirect2SummitPage() {
        Uri uri = Uri.parse("https://www.openstack.org/summit");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        view.startActivity(intent);
    }

    @Override
    public void redirect2CodeConductPage() {
        Uri uri = Uri.parse("https://www.openstack.org/code-of-conduct/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        view.startActivity(intent);
    }

    @Override
    public void sendInquireEmail() {
        Intent emailintent = new Intent(Intent.ACTION_SEND);
        emailintent.setType("plain/text");
        emailintent.putExtra(Intent.EXTRA_EMAIL,new String[] {"summit@openstack.org" });
        emailintent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailintent.putExtra(Intent.EXTRA_TEXT,"");
        view.startActivity(Intent.createChooser(emailintent, "Send mail..."));
    }
}
