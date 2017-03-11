package org.openstack.android.summit.modules.about.user_interface;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.linearlistview.LinearListView;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.DTOs.WifiListItemDTO;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.openstack.android.summit.common.user_interface.WifiItemView;

import java.util.List;

/**
 * Created by Claudio Redi on 4/1/2016.
 */
public class AboutFragment extends BaseFragment<IAboutPresenter> implements IAboutView {

    private WifiListAdapter wifiListAdapter;

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
        view = inflater.inflate(R.layout.fragment_about, container, false);

        LinearListView wifiList = (LinearListView)view.findViewById(R.id.about_wifi_networks_list);
        wifiListAdapter = new WifiListAdapter(getContext());
        wifiList.setAdapter(wifiListAdapter);

        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        setVersion(String.format("Version %s", pInfo.versionName));

        setBuild(String.format("Build Number %s", pInfo.versionCode));

        TextView websiteLink = (TextView)view.findViewById(R.id.about_website_link);
        websiteLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.openstack.org");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        TextView conductLink = (TextView)view.findViewById(R.id.about_conduct_link);
        conductLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.openstack.org/code-of-conduct/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        TextView supportLink = (TextView)view.findViewById(R.id.about_support_link);
        supportLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailintent = new Intent(android.content.Intent.ACTION_SEND);
                emailintent.setType("plain/text");
                emailintent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] {"summitapp@openstack.org" });
                emailintent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                emailintent.putExtra(android.content.Intent.EXTRA_TEXT,"");
                startActivity(Intent.createChooser(emailintent, "Send mail..."));
            }
        });

        TextView inquiriesLink = (TextView)view.findViewById(R.id.about_inquiries_link);
        inquiriesLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailintent = new Intent(android.content.Intent.ACTION_SEND);
                emailintent.setType("plain/text");
                emailintent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] {"summit@openstack.org" });
                emailintent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                emailintent.putExtra(android.content.Intent.EXTRA_TEXT,"");
                startActivity(Intent.createChooser(emailintent, "Send mail..."));
            }
        });

        presenter.onCreateView(savedInstanceState);
        return view;
    }

    @Override
    public void setSummitName(String name) {
        TextView nameText = (TextView)view.findViewById(R.id.about_summit_name);
        nameText.setText(name);
    }

    @Override
    public void setSummitDate(String date) {
        TextView dateText = (TextView)view.findViewById(R.id.about_summit_date);
        dateText.setText(date);
    }


    @Override
    public void setVersion(String version) {
        TextView textView = (TextView)view.findViewById(R.id.about_version_text);
        textView.setText(version);
    }

    @Override
    public void setBuild(String build) {
        TextView textView = (TextView)view.findViewById(R.id.about_build_text);
        textView.setText(build);
    }

    @Override
    public void setWifiConnections(List<WifiListItemDTO> wifiConnections) {
        wifiListAdapter.clear();
        wifiListAdapter.addAll(wifiConnections);

        LinearListView wifiList = (LinearListView)view.findViewById(R.id.about_wifi_networks_list);

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
}
