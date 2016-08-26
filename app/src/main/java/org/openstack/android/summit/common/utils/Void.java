package org.openstack.android.summit.common.utils;

import android.content.Context;

/**
 * Created by sebastian on 8/25/2016.
 */
public class Void extends Object{
    private Void() {}

    private static final Object mSingletonLock = new Object();
    private static Void instance;

    public static Void getInstance() {
        synchronized (mSingletonLock) {
            if (instance != null)
                return instance;
            instance = new Void();
            return instance;
        }
    }
}
