package org.openstack.android.summit.common;

import android.content.Context;
import android.content.SharedPreferences;

import org.openstack.android.summit.OpenStackSummitApplication;

/**
 * Created by Claudio Redi on 1/5/2016.
 */
public class Session implements ISession {
    @Override
    public int getInt(String key) {
        SharedPreferences preferences = OpenStackSummitApplication.context.getSharedPreferences(Constants.LOG_TAG, Context.MODE_PRIVATE);
        return preferences.getInt(key, 0);
    }

    @Override
    public void setInt(String key, int value) {
        SharedPreferences preferences = OpenStackSummitApplication.context.getSharedPreferences(Constants.LOG_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    @Override
    public String getString(String key) {
        SharedPreferences preferences = OpenStackSummitApplication.context.getSharedPreferences(Constants.LOG_TAG, Context.MODE_PRIVATE);
        return preferences.getString(key, null);
    }

    @Override
    public void setString(String key, String value) {
        SharedPreferences preferences = OpenStackSummitApplication.context.getSharedPreferences(Constants.LOG_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
