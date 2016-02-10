package org.openstack.android.summit.common.data_access.data_polling;

import org.json.JSONException;

/**
 * Created by Claudio Redi on 2/7/2016.
 */
public interface IDataUpdateProcessor {
    void process(String json) throws JSONException;
}
