package org.openstack.android.summit.common.data_access;

import org.openstack.android.summit.BuildConfig;
import org.openstack.android.summit.common.Constants;

/**
 * Created by Claudio Redi on 3/7/2016.
 */
public class BaseRemoteDataStore {
    protected String getResourceServerUrl() {
        String resourceServerUrl = "";
        if (BuildConfig.DEBUG) {
            resourceServerUrl = Constants.TEST_RESOURCE_SERVER_BASE_URL;
        }
        else {
            resourceServerUrl = Constants.PRODUCTION_RESOURCE_SERVER_BASE_URL;
        }
        return resourceServerUrl;
    }
}
