package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.BuildConfig;
import org.openstack.android.summit.common.Constants;

/**
 * Created by Claudio Redi on 3/7/2016.
 */
public class BaseRemoteDataStore {

    protected String baseResourceServerUrl;

    public String getBaseResourceServerUrl() {
        return baseResourceServerUrl;
    }

    public void setBaseResourceServerUrl(String baseResourceServerUrl) {
        this.baseResourceServerUrl = baseResourceServerUrl;
    }
}
