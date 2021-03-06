package org.openstack.android.summit.modules.about.user_interface;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linearlistview.LinearListView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.WifiListItemDTO;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.WifiItemView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Claudio Redi on 4/1/2016.
 */
public class AboutFragment extends BaseFragment<IAboutPresenter> implements IAboutView {

    private WifiListAdapter wifiListAdapter;
    private Unbinder unbinder;

    @BindView(R.id.about_wifi_networks_list)
    LinearListView wifiList;

    @BindView(R.id.about_website_link)
    TextView websiteLink;

    @BindView(R.id.about_conduct_link)
    TextView conductLink;

    @BindView(R.id.about_support_link)
    TextView supportLink;

    @BindView(R.id.about_inquiries_link)
    TextView inquiriesLink;

    @BindView(R.id.about_summit_name)
    TextView nameText;

    @BindView(R.id.about_summit_date)
    TextView dateText;

    @BindView(R.id.about_version_text)
    TextView aboutText;

    @BindView(R.id.about_build_text)
    TextView aboutBuildText;

    @BindView(R.id.wifi_networks_container)
    LinearLayout wifiContainer;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(getResources().getString(R.string.about));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        presenter.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view     = inflater.inflate(R.layout.fragment_about, container, false);
        unbinder = ButterKnife.bind(this, view);

        wifiListAdapter = new WifiListAdapter(getContext());
        wifiList.setAdapter(wifiListAdapter);

        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        setVersion(String.format(Locale.US, "Version %s", pInfo.versionName));

        setBuild(String.format(Locale.US, "Build Number %s", pInfo.versionCode));

        websiteLink.setOnClickListener(v -> {
           presenter.redirect2SummitPage();
        });

        conductLink.setOnClickListener(v -> {
            presenter.redirect2CodeConductPage();
        });

        supportLink.setOnClickListener(v -> {
            presenter.redirect2IssueTrackerPage();
        });

        inquiriesLink.setOnClickListener(v -> {
           presenter.sendInquireEmail();
        });

        presenter.onCreateView(savedInstanceState);
        return view;
    }

    @Override
    public void setSummitName(String name) {
        if(nameText == null) return;
        nameText.setText(name);
    }

    @Override
    public void setSummitDate(String date) {
        if(dateText == null) return;
        dateText.setText(date);
    }

    @Override
    public void setVersion(String version) {
        if(aboutText == null) return;
        aboutText.setText(version);
    }

    @Override
    public void setBuild(String build) {
        if(aboutBuildText == null) return;
        aboutBuildText.setText(build);
    }

    @Override
    public void setWifiConnections(List<WifiListItemDTO> wifiConnections) {
        wifiListAdapter.clear();
        wifiListAdapter.addAll(wifiConnections);
        wifiContainer.setVisibility(wifiConnections.size() == 0 ? view.GONE : view.VISIBLE);
    }

    @Override
    public void hideWifiConnections() {
        wifiContainer.setVisibility(view.GONE);
    }

    private class WifiListAdapter extends ArrayAdapter<WifiListItemDTO> {

        public WifiListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_wifi_list, parent, false);
            }

            final WifiItemView wifiItemView = new WifiItemView(convertView);

            presenter.buildWifiListItem(wifiItemView, position);

            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        };
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        if(unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }

}
