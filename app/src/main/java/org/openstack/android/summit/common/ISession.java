package org.openstack.android.summit.common;

/**
 * Created by Claudio Redi on 1/5/2016.
 */
public interface ISession {
    int getInt(String key);

    void setInt(String key, int value);

    void setLong(String key, long value);

    String getString(String key);

    void setString(String key, String value);

    long getLong(String key_set_from_date);
}
