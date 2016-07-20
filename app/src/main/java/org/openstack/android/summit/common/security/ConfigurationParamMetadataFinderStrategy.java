package org.openstack.android.summit.common.security;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by sebastian on 7/18/2016.
 */
final public class ConfigurationParamMetadataFinderStrategy implements IConfigurationParamFinderStrategy {

    private Context context;
    private final String TAG = getClass().getSimpleName();

    public ConfigurationParamMetadataFinderStrategy(Context context){
        this.context = context;
    }

    @Override
    public String find(String key) {
        String value = "";
        try {
            //Context ctx = OpenStackSummitApplication.context;
            PackageManager pkgManager =  context.getPackageManager();
            ApplicationInfo ai = pkgManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            value = bundle.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        return value;
    }
}
