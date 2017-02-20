package org.openstack.android.summit.common.user_interface;

import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.openstack.android.summit.R;

/**
 * Created by Claudio Redi on 1/13/2016.
 */
public class WifiItemView implements IWifiItemView {
    private View view;

    public WifiItemView(View view) {
        this.view = view;
    }

    @Override
    public void setSsid(String ssid) {
        TextView ssidTextView = (TextView) view.findViewById(R.id.item_wifi_list_ssid);
        ssidTextView.setText(ssid);
    }

    @Override
    public void setPassword(String password) {
        TextView passwordTextView = (TextView) view.findViewById(R.id.item_wifi_list_password);
        passwordTextView.setText(password);
    }
}
