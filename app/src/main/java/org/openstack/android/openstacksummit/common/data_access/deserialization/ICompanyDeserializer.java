package org.openstack.android.openstacksummit.common.data_access.deserialization;

import org.json.JSONException;
import org.json.JSONObject;
import org.openstack.android.openstacksummit.common.entities.Company;

/**
 * Created by Claudio Redi on 11/5/2015.
 */
public interface ICompanyDeserializer {
    Company deserialize(JSONObject jsonObject) throws JSONException;
}
