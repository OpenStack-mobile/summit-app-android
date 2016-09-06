package org.openstack.android.summit.modules.about.user_interface;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.openstack.android.summit.R;
import org.openstack.android.summit.common.user_interface.BaseFragment;
import org.w3c.dom.Text;

/**
 * Created by Claudio Redi on 4/1/2016.
 */
public class AboutFragment extends BaseFragment<IAboutPresenter> implements IAboutView {

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

        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        setVersion(String.format("Version %s(%s)",pInfo.versionName, pInfo.versionCode));

        TextView websiteLink = (TextView)view.findViewById(R.id.about_website_link);
        websiteLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.openstack.org");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        presenter.onCreateView(savedInstanceState);
        return view;
    }

    @Override
    public void setSummitNameAndDate(String nameAndDate) {
        TextView nameAndDateText = (TextView)view.findViewById(R.id.about_summit_name_and_date);
        nameAndDateText.setText(nameAndDate);
    }

    @Override
    public void setVersion(String version) {
        TextView textView = (TextView)view.findViewById(R.id.about_version_text);
        textView.setText(version);
    }
}
