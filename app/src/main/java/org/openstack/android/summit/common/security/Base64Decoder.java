package org.openstack.android.summit.common.security;

import android.util.Base64;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.openstack.android.summit.BuildConfig;
import org.openstack.android.summit.common.Constants;

import java.io.UnsupportedEncodingException;

/**
 * Created by Claudio Redi on 4/8/2016.
 */
public class Base64Decoder implements IDecoder {
    @Override
    public String decode(String codedString) throws UnsupportedEncodingException {
        byte[] data = Base64.decode(codedString, Base64.DEFAULT);
        String text = new String(data, "UTF-8");
        return text;
    }
}
