package org.openstack.android.summit.common.user_interface;

import android.widget.Button;
import android.widget.LinearLayout;

import org.openstack.android.summit.R;

import butterknife.BindView;

/**
 * Created by smarcet on 2/8/18.
 */

public class BaseDataLoadingActivity extends BaseActivity {

    @BindView(R.id.initial_data_loading_retry_button)
    protected Button retryButton;

    @BindView(R.id.initial_data_loading_no_conectivity)
    protected LinearLayout initialDataLoginNoConnectivity;
}
