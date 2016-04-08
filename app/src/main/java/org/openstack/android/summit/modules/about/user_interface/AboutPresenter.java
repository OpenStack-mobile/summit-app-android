package org.openstack.android.summit.modules.about.user_interface;

import android.content.pm.PackageInfo;
import android.os.Bundle;

import org.openstack.android.summit.common.DTOs.SummitDTO;
import org.openstack.android.summit.common.business_logic.IBaseInteractor;
import org.openstack.android.summit.common.entities.Summit;
import org.openstack.android.summit.common.user_interface.BasePresenter;
import org.openstack.android.summit.modules.about.IAboutWireframe;
import org.openstack.android.summit.modules.about.business_logic.IAboutInteractor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by Claudio Redi on 4/1/2016.
 */
public class AboutPresenter extends BasePresenter<IAboutView, IAboutInteractor, IAboutWireframe> implements IAboutPresenter {
    public AboutPresenter(IAboutInteractor interactor, IAboutWireframe wireframe) {
        super(interactor, wireframe);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        SummitDTO summit = interactor.getActiveSummit();
        String nameAndDate = buildNameDate(summit);
        view.setSummitNameAndDate(nameAndDate);
    }

    private String buildNameDate(SummitDTO summit) {
        String  nameAndDate = summit.getName();

        DateFormat formatterFrom = new SimpleDateFormat("MMMM dd-");
        formatterFrom.setTimeZone(TimeZone.getTimeZone(summit.getTimeZone()));

        DateFormat formatterTo = new SimpleDateFormat("dd, yyyy");
        formatterTo.setTimeZone(TimeZone.getTimeZone(summit.getTimeZone()));

        String timeRange = String.format("%s%s", formatterFrom.format(summit.getStartDate()), formatterTo.format(summit.getEndDate()));

        nameAndDate += " - " + timeRange;
        return nameAndDate;
    }
}
